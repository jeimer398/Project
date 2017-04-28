package project;

import javax.swing.*;

/**
 * Created by bengw on 4/28/2017.
 */
public class GUIMediator {

    private MachineModel model;

    public void step() {} //complete later

    public JFrame getFrame() {
        return null; //temporary
    }

    public void clearJob() {} //complete later

    public void makeReady(String s) {} //complete later

    public MachineModel getModel() {
        return model;
    }

    public void setModel(MachineModel model) {
        this.model = model;
    }
}
