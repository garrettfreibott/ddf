package org.codice.ui.admin.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static spark.Spark.after;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;

import org.apache.http.ExceptionLogger;
import org.apache.http.HttpStatus;
import org.codice.ui.admin.security.api.ConfigurationHandler;
import org.codice.ui.admin.security.api.StageFactory;
import org.codice.ui.admin.security.api.Wizard;
import org.codice.ui.admin.security.stage.Stage;
import org.codice.ui.admin.security.stage.StageParameters;
import org.codice.ui.admin.security.stage.components.ButtonActionComponent;
import org.codice.ui.admin.security.stage.components.Component;
import org.codice.ui.admin.security.stage.components.HostnameComponent;
import org.codice.ui.admin.security.stage.components.InfoComponent;
import org.codice.ui.admin.security.stage.components.PasswordComponent;
import org.codice.ui.admin.security.stage.components.PortComponent;
import org.codice.ui.admin.security.stage.components.StringComponent;
import org.codice.ui.admin.security.stage.components.StringEnumComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import spark.ExceptionHandler;
import spark.Request;
import spark.servlet.SparkApplication;

public class WizardRouter implements SparkApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(WizardRouter.class);

    public static final String APPLICATION_JSON = "application/json";

    public static String contextPath;

    private List<Wizard> wizards;

    private List<StageFactory> stages;

    private List<ConfigurationHandler> configurationHandlers;

    private Optional<Wizard> findWizard(String id) {
        return wizards.stream()
                .filter(w -> w.getWizardId()
                        .equals(id))
                .findFirst();
    }

    public String getContextPath() {
        return contextPath;
    }

    private static Map<String, Object> toMap(Wizard w) {
        // @formatter:off
        return ImmutableMap.of(
                "id", w.getWizardId(),
                "title", w.getTitle(),
                "description", w.getDescription());
        // @formatter:on
    }

    @Override
    public void init() {

        get("/", (req, res) -> {
            return wizards.stream()
                    .map(WizardRouter::toMap)
                    .collect(Collectors.toList());
        }, new Gson()::toJson);

        get("/:wizardId", (req, res) -> {
            Optional<Wizard> wizard = findWizard(req.params(":wizardId"));

            if (!wizard.isPresent()) {
                res.status(404);
                return null;
            }

            return wizard.get()
                    .getStageComposer(req.params(":wizardId"), stages, configurationHandlers)
                    .findStage(wizard.get()
                                    .initialStageId(),
                            new StageParameters(getContextPath() + "/" + req.params(":wizardId")));

        }, getGsonParser()::toJson);

        post("/:wizardId/:stageId", (req, res) -> {

            Optional<Wizard> wizard = findWizard(req.params(":wizardId"));

            if (!wizard.isPresent()) {
                res.status(404);
                return null;
            }

            Stage nextStage = wizard.get()
                    .getStageComposer(getContextPath() + "/" + req.params(":wizardId"),
                            stages,
                            configurationHandlers)
                    .processStage(getStageFromRequest(req), req.params());

            if (nextStage.containsError()) {
                res.status(HttpStatus.SC_BAD_REQUEST);
            }
            return nextStage;
        }, getGsonParser()::toJson);

        after("/*", (req, res) -> res.type(APPLICATION_JSON));

        exception(Exception.class, (ex, req, res) -> {
            LOGGER.error("Wizard router error: ", ex);
            // TODO: tbatie - 10/26/16 - Remove this on merge
            res.status(500);
            res.body(exToJSON(ex));
        });
    }

    private String exToJSON(Exception ex) {
        Map<String, Object> e = new HashMap<>();
        e.put("stackTrace", ex.getStackTrace());
        e.put("cause", ex.getCause());
        return new Gson().toJson(e);
    }

    public Stage getStageFromRequest(Request req) {
        Stage stageFoundById = stages.stream()
                .filter(stageFactory -> stageFactory.getStageId()
                        .equals(req.params(":stageId")))
                .findFirst()
                .get()
                .getNewInstance(new StageParameters(null));

        return getGsonParser().fromJson(req.body(), stageFoundById.getClass());
    }

    private static Gson getGsonParser() {

        RuntimeTypeAdapterFactory rtaf = RuntimeTypeAdapterFactory.of(Component.class, "type")
                .registerSubtype(ButtonActionComponent.class, "BUTTON_ACTION")
                .registerSubtype(HostnameComponent.class, "HOSTNAME")
                .registerSubtype(PasswordComponent.class, "PASSWORD")
                .registerSubtype(PortComponent.class, "PORT")
                .registerSubtype(StringEnumComponent.class, "STRING_ENUM")
                .registerSubtype(StringComponent.class, "STRING")
                .registerSubtype(Component.class, "BASE_CONTAINER")
                .registerSubtype(InfoComponent.class, "INFO");

        return new GsonBuilder().registerTypeAdapterFactory(rtaf)
                .create();
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public void setStages(List<StageFactory> stages) {
        this.stages = stages;
    }

    public void setWizards(List<Wizard> wizards) {
        this.wizards = wizards;
    }

    public void setConfigurationHandlers(List<ConfigurationHandler> configurationHandlers) {
        this.configurationHandlers = configurationHandlers;
    }
}
