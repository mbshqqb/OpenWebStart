package com.openwebstart.proxy.ui;

import net.adoptopenjdk.icedteaweb.client.controlpanel.panels.provider.ControlPanelProvider;
import net.adoptopenjdk.icedteaweb.i18n.Translator;
import net.sourceforge.jnlp.config.DeploymentConfiguration;

import javax.swing.JComponent;

public class ProxyConfigPanelProvider implements ControlPanelProvider {

    @Override
    public String getTitle() {
        return Translator.getInstance().translate("proxyPanel.title");
    }

    @Override
    public String getName() {
        return "ProxyConfig";
    }

    @Override
    public int getOrder() {
        return 31;
    }

    @Override
    public JComponent createPanel(final DeploymentConfiguration deploymentConfiguration) {
        return new ProxyConfigPanel(deploymentConfiguration);
    }
}

