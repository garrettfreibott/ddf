package org.codice.ui.admin.security;

import static org.boon.HTTP.APPLICATION_JSON;
import static org.codice.ui.admin.security.stage.sample.LdapBindHostSettingsStage.LDAP_BIND_HOST_SETTINGS_STAGE_ID;
import static org.codice.ui.admin.security.stage.sample.LdapDirectorySettingsStage.LDAP_DIRECTORY_SETTINGS_STAGE_ID;
import static org.codice.ui.admin.security.stage.sample.LdapNetworkSettingsStage.LDAP_NETWORK_SETTINGS_STAGE_ID;
import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.codice.ui.admin.security.api.StageFactory;
import org.codice.ui.admin.security.api.Wizard;
import org.codice.ui.admin.security.stage.Stage;
import org.codice.ui.admin.security.stage.StageComposer;
import org.codice.ui.admin.security.stage.StageParameters;
import org.codice.ui.admin.security.stage.components.ButtonActionComponent;
import org.codice.ui.admin.security.stage.components.Component;
import org.codice.ui.admin.security.stage.components.HostnameComponent;
import org.codice.ui.admin.security.stage.components.PasswordComponent;
import org.codice.ui.admin.security.stage.components.PortComponent;
import org.codice.ui.admin.security.stage.components.StringComponent;
import org.codice.ui.admin.security.stage.components.StringEnumComponent;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import spark.Request;
import spark.servlet.SparkApplication;

public class LdapWizard implements SparkApplication, Wizard {

    public static String contextPath;

    private StageComposer stageComposer;

    private List<StageFactory> stages;

    public void setWizards(List<Wizard> wizards) {
        this.wizards = wizards;
    }

    private List<Wizard> wizards;

    private Optional<Wizard> findWizard(String id) {
        return wizards.stream()
                .filter(w -> w.getId()
                        .equals(id))
                .findFirst();
    }

    @Override
    public String getTitle() {
        return "LDAP Wizard";
    }

    @Override
    public String getDescription() {
        return "Help setup that thing called LDAP!";
    }

    public String getContextPath() {
        return contextPath;
    }

    @Override
    public String initialStageId() {
        return LDAP_NETWORK_SETTINGS_STAGE_ID;
    }

    private static Map<String, Object> toMap(Wizard w) {
        // @formatter:off
        return ImmutableMap.of(
                "id", w.getId(),
                "title", w.getTitle(),
                "description", w.getDescription());
        // @formatter:on
    }

    @Override
    public void init() {

        stageComposer = getStageComposer(contextPath, stages);

        get("/", (req, res) -> {
            return wizards.stream()
                    .map(LdapWizard::toMap)
                    .collect(Collectors.toList());
        }, new Gson()::toJson);

        get("/:wizardId", (req, res) -> {
            // TODO: tbatie - 10/24/16 - When the stage is not found should we return the init stage by default?

            Optional<Wizard> wizard = findWizard(req.params(":wizardId"));

            if (!wizard.isPresent()) {
                res.status(404);
                return null;
            }

            return wizard.get()
                    .getStageComposer(req.params(":wizardId"), stages)
                    .findStage(wizard.get().initialStageId(),
                            new StageParameters(getContextPath() + "/" + req.params(":wizardId")));

        }, getGsonParser()::toJson);

        post("/:wizardId/:stageId", (req, res) -> {

            Optional<Wizard> wizard = findWizard(req.params(":wizardId"));

            if (!wizard.isPresent()) {
                res.status(404);
                return null;
            }

            Stage nextStage = wizard.get()
                    .getStageComposer(getContextPath() + "/" + req.params(":wizardId"), stages)
                    .processStage(getStageFromRequest(req), req.params());

            if (nextStage.containsError()) {
                res.status(HttpStatus.SC_BAD_REQUEST);
            }
            return nextStage;
        }, getGsonParser()::toJson);

        after("/*", (req, res) -> res.type(APPLICATION_JSON));
    }

    public StageComposer getStageComposer(String contextPath, List<StageFactory> allStages) {
        return StageComposer.builder(contextPath, allStages)
                .link(LDAP_NETWORK_SETTINGS_STAGE_ID, LDAP_BIND_HOST_SETTINGS_STAGE_ID)
                .link(LDAP_BIND_HOST_SETTINGS_STAGE_ID, LDAP_DIRECTORY_SETTINGS_STAGE_ID);
    }

    @Override
    public String getId() {
        return "ldap";
    }

    public Stage getStageFromRequest(Request req) {
        // TODO: tbatie - 10/24/16 - This is a sloppy workaround, can't call get class on this reference list, you'll get a proxy class back. Instead, calling newInstance and grabbing class from that
        // TODO: tbatie - 10/24/16 - Null checks, logging, all that good stuff
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
                .registerSubtype(Component.class, "BASE_CONTAINER");

        return new GsonBuilder().registerTypeAdapterFactory(rtaf)
                .create();
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public void setStages(List<StageFactory> stages) {
        this.stages = stages;
    }
}
