// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.actions.metadata;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.properties.SalesforceSchemaConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.core.ui.images.OverlayImageProvider;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.wizards.metadata.connection.files.salesforce.SalesforceSchemaWizard;

/**
 * DOC yexiaowei class global comment. Detailled comment
 */

public class CreateSalesforceSchemaAction extends AbstractCreateAction {

    private static final String EDIT_LABEL = Messages.getString("CreateSalesforceSchemaAction.editTitle"); //$NON-NLS-1$

    private static final String CREATE_LABEL = Messages.getString("CreateSalesforceSchemaAction.action.createTitle"); //$NON-NLS-1$

    private static final String OPEN_LABEL = Messages.getString("CreateSalesforceSchemaAction.action.openTitle"); //$NON-NLS-1$

    protected static final int WIZARD_WIDTH = 800;

    protected static final int WIZARD_HEIGHT = 475;

    private boolean creation = false;

    ImageDescriptor defaultImage = ImageProvider.getImageDesc(ECoreImage.METADATA_SALESFORCE_SCHEMA_ICON);

    ImageDescriptor createImage = OverlayImageProvider.getImageWithNew(ImageProvider
            .getImage(ECoreImage.METADATA_SALESFORCE_SCHEMA_ICON));

    public CreateSalesforceSchemaAction() {
        super();

        this.setText(CREATE_LABEL);
        this.setToolTipText(CREATE_LABEL);
        this.setImageDescriptor(defaultImage);

    }

    public CreateSalesforceSchemaAction(boolean isToolbar) {
        super();
        setToolbar(isToolbar);
        this.setText(CREATE_LABEL);
        this.setToolTipText(CREATE_LABEL);
        this.setImageDescriptor(defaultImage);
    }

    public void run() {

        RepositoryNode salesforceSchema = getCurrentRepositoryNode();

        if (isToolbar()) {
            if (salesforceSchema != null && salesforceSchema.getContentType() != ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA) {
                salesforceSchema = null;
            }
            if (salesforceSchema == null) {
                salesforceSchema = getRepositoryNodeForDefault(ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA);
            }
        }
        ISelection selection = null;
        WizardDialog wizardDialog = null;
        if (isToolbar()) {
            init(salesforceSchema);
            wizardDialog = new WizardDialog(new Shell(), new SalesforceSchemaWizard(PlatformUI.getWorkbench(), creation,
                    selection, getExistingNames(), false));// TODO send schema
        } else {
            selection = getSelection();
            wizardDialog = new WizardDialog(new Shell(), new SalesforceSchemaWizard(PlatformUI.getWorkbench(), creation,
                    selection, getExistingNames(), false));
        }

        wizardDialog.setPageSize(WIZARD_WIDTH, WIZARD_HEIGHT);
        wizardDialog.create();
        wizardDialog.open();
        if (isToolbar()) {
            refresh(salesforceSchema);
        } else {
            refresh(((IStructuredSelection) selection).getFirstElement());
        }

    }

    protected void init(RepositoryNode node) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (!ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA.equals(nodeType)) {
            return;
        }

        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            if (ProxyRepositoryFactory.getInstance().isUserReadOnlyOnCurrentProject()) {
                setEnabled(false);
                return;
            }
            this.setText(CREATE_LABEL);
            collectChildNames(node);
            creation = true;
            this.setImageDescriptor(createImage);
            break;
        case REPOSITORY_ELEMENT:
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            if (factory.isPotentiallyEditable(node.getObject())) {
                this.setText(EDIT_LABEL);
                this.setImageDescriptor(defaultImage);
                collectSiblingNames(node);
            } else {
                this.setText(OPEN_LABEL);
                this.setImageDescriptor(defaultImage);
            }
            collectSiblingNames(node);
            creation = false;
            break;
        default:
            return;
        }
        setEnabled(true);
    }

    public Class getClassForDoubleClick() {
        return SalesforceSchemaConnectionItem.class;
    }
}
