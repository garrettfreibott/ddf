package org.codice.ui.admin.security.stage;

import static org.codice.ui.admin.security.stage.Stage.NEXT_STAGE_ID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.codice.ui.admin.security.api.ConfigurationHandler;
import org.codice.ui.admin.security.api.StageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class StageComposer {

    private static final Logger LOGGER = LoggerFactory.getLogger(StageComposer.class);

    private String wizardUrl;

    private List<StageFactory> stages;

    private List<ConfigurationHandler> configurationHandlers;

    private Map<String, Function<Stage, String>> stageLinks = new HashMap<>();

    public StageComposer(String wizardUrl, List<StageFactory> stages, List<ConfigurationHandler> configurationHandlers) {
        this.wizardUrl = wizardUrl;
        this.stages = stages;
        this.configurationHandlers = configurationHandlers;
        stageLinks = new HashMap<>();
    }

    /**
     * Validates fields of stage. If there are no errors from validation, then the fields are tested.
     * If there are no errors resulting from testing, then the state and configuration are persisted then the next stage is looked up
     *
     * @param stageToProcess - Stage to be validated, tested and used as a look up for the
     * @param params         - Additional parameters used during validation and testing from the request
     * @return Stage with errors that must be addressed or the next corresponding stage
     */
    public Stage processStage(Stage stageToProcess, Map<String, String> params) {
        stageToProcess.clearErrors();

        Stage validatedStage = stageToProcess.validateStage(stageToProcess, params);
        if (validatedStage.containsError()) {
            return validatedStage;
        }

        Stage testedStage = stageToProcess.testStage(stageToProcess, configurationHandlers, params);
        if (testedStage.containsError()) {
            return testedStage;
        }

        Stage stageWithNewState = stageToProcess.commitStage(stageToProcess, params);

        if (testedStage.containsError()) {
            return testedStage;
        }

        return lookUpNextStage(stageWithNewState, params);
    }

    /**
     * Traverses the list of stages to find the next stage according to it's linked stage id's. If the nextStageId is present in the previousStage state, that will be used instead of the link look up
     *
     * @param previousStage - The previous stage that is the link to the new stage
     * @param params
     * @return
     */
    public Stage lookUpNextStage(Stage previousStage, Map<String, String> params) {
        String linkedStageId = null;

        if (previousStage.getState() != null && previousStage.getState()
                .containsKey(NEXT_STAGE_ID)) {
            linkedStageId = previousStage.getState()
                    .get(NEXT_STAGE_ID);
        }

        if (StringUtils.isEmpty(linkedStageId)) {
            if (stageLinks.get(previousStage.getStageId()) == null) {
                LOGGER.error("No follow up stage link or next stage id found for {}",
                        previousStage.getStageId());
            }


        linkedStageId = stageLinks.get(previousStage.getStageId())
                    .apply(previousStage);
        }
         Stage foundStage = findStage(linkedStageId,
                 new StageParameters(wizardUrl,
                         previousStage.getState(),
                         previousStage.getConfiguration()));

        return foundStage.preconfigureStage(foundStage, configurationHandlers);
    }

    //
    //  Builder Methods
    //
    public static StageComposer builder(String wizardUrl, List<StageFactory> stages, List<ConfigurationHandler> configurationHandlers) {
        return new StageComposer(wizardUrl, stages, configurationHandlers);
    }

    public StageComposer link(String origin, String destination) {
        stageLinks.put(origin, (m) -> destination);
        return this;
    }

    public StageComposer link(String origin, Function<Stage, String> nextStageCondition) {
        stageLinks.put(origin, nextStageCondition);
        return this;
    }

    public Stage findStage(String stageId, StageParameters stageParameters) {
        Optional<StageFactory> linkedStage = stages.stream()
                .filter(stage -> stage.getStageId()
                        .equals(stageId))
                .findFirst();

        if (linkedStage.isPresent()) {
            return linkedStage.get()
                    .getNewInstance(stageParameters);
        }

        return null;
    }
}
