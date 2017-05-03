package project;

import javax.swing.Timer;

/**
 * Created by bengw on 4/28/2017.
 */
public class StepControl {

    private static final int TICK = 500;
    private boolean autoStepOn = false;
    Timer timer;
    GUIMediator gui;

    public StepControl(GUIMediator gui) {
		super();
		this.gui = gui;
	}

	public void toggleAutoStepOn() {
        if (autoStepOn) {
            autoStepOn = false;
        } else {
            autoStepOn = true;
        }
    }

    public void setPeriod(int period) {
        timer.setDelay(period);
    }

    public void start() {
        timer = new Timer(TICK, e -> {if(autoStepOn) gui.step();});
        timer.start();
    }

    public boolean isAutoStepOn() {
        return autoStepOn;
    }

    public void setAutoStepOn(boolean autoStepOn) {
        this.autoStepOn = autoStepOn;
    }
}
