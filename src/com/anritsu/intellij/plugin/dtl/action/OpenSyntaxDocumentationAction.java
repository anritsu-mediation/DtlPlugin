package com.anritsu.intellij.plugin.dtl.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class OpenSyntaxDocumentationAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent event) {
        Project currentProject = event.getProject();
        event.getPresentation().setEnabledAndVisible(currentProject != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Desktop desktop = java.awt.Desktop.getDesktop();
        try {
            URI oURL = new URI("https://anritsuglobal.sharepoint.com/:w:/r/sites/EU-0017/_layouts/15/Doc.aspx?sourcedoc=%7B4EBCA761-70B4-4C10-BE15-18640A6444E2%7D&file=DTL-sds.doc&action=default&mobileredirect=true");
            desktop.browse(oURL);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

}