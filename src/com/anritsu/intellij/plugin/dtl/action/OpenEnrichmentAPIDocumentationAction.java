package com.anritsu.intellij.plugin.dtl.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class OpenEnrichmentAPIDocumentationAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent event) {
        Project currentProject = event.getProject();
        event.getPresentation().setEnabledAndVisible(currentProject != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Desktop desktop = Desktop.getDesktop();
        try {
            URI oURL = new URI("https://anritsuglobal.sharepoint.com/sites/EU-0017/Product%20Documents/Forms/AllItems.aspx?id=%2Fsites%2FEU%2D0017%2FProduct%20Documents%2FeoXDR%20Server%2FDtlApi%2DEnrichment%2Ehtml&parent=%2Fsites%2FEU%2D0017%2FProduct%20Documents%2FeoXDR%20Server");
            desktop.browse(oURL);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

}