package project;

import javax.swing.*;
import java.util.Observable;

public class GUIMediator extends Observable{
	private MachineModel model;
	private FilesMgr filesMgr;
	private StepControl stepControl;
	private JFrame frame;

	public void step(){
		if(model.getCurrentState() != States.PROGRAM_HALTED && model.getCurrentState() != States.NOTHING_LOADED){
			try{
				model.step();
			}catch (CodeAccessException e) {
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to code from line " + model.getpCounter() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			}catch (ArrayIndexOutOfBoundsException e){
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to data" + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			}catch (NullPointerException e){
				JOptionPane.showMessageDialog(
						frame,
						"Illegal access to data" + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			}catch (IllegalArgumentException e){
				JOptionPane.showMessageDialog(
						frame,
						"Illegal access to code" + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			}catch (DivideByZeroException e){
				JOptionPane.showMessageDialog(
						frame,
						"Illegal access to code " + model.getpCounter() + "\n"
						+ "Exception message: " + e.getMessage(),
						"Divide by zero error",
						JOptionPane.OK_OPTION);
			}
			setChanged();
			notifyObservers();
		}
	}

	public void execute(){
		while(model.getCurrentState() != States.PROGRAM_HALTED && model.getCurrentState() != States.NOTHING_LOADED){
			try{
				model.step();
			}catch (CodeAccessException e) {
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to code from line " + model.getpCounter() + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			}catch (ArrayIndexOutOfBoundsException e){
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to data" + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			}catch (NullPointerException e){
				JOptionPane.showMessageDialog(
						frame,
						"Illegal access to data" + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			}catch (IllegalArgumentException e){
				JOptionPane.showMessageDialog(
						frame,
						"Illegal access to code" + "\n"
								+ "Exception message: " + e.getMessage(),
								"Run time error",
								JOptionPane.OK_OPTION);
			}catch (DivideByZeroException e){
				JOptionPane.showMessageDialog(
						frame,
						"Illegal access to code " + model.getpCounter() + "\n"
						+ "Exception message: " + e.getMessage(),
						"Divide by zero error",
						JOptionPane.OK_OPTION);
			}
		}
		setChanged();
		notifyObservers();
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public void clearJob() {
		model.clearJob();
		model.setCurrentState(States.NOTHING_LOADED);
		model.getCurrentState().enter();
		setChanged();
		notifyObservers("Clear");
	}

	public void toggleAutoStep(){
		stepControl.toggleAutoStep();
		if(stepControl.isAutoStepOn()){
			model.setCurrentState(States.AUTO_STEPPING);
		}else{
			model.setCurrentState(States.PROGRAM_LOADED_NOT_AUTOSTEPPING);
		}
		model.getCurrentState().enter();
		setChanged();
		notifyObservers();
	}

	public void reload(){
		stepControl.setAutoStepOn(false);
		clearJob();
		filesMgr.finalLoad_ReloadStep(model.getCurrentJob());
	}

	public void assembleFile(){
		filesMgr.loadFile(model.getCurrentJob());
	}

	public void setPeriod(int period){
		stepControl.setPeriod(period);
	}

	public void changeToJob(int i){
		model.changeToJob(i);
		if(model.getCurrentState()!=null){
			model.getCurrentState().enter();
			setChanged();
			notifyObservers();
		}
	}

	public void makeReady(String string){
		stepControl.setAutoStepOn(false);
		model.setCurrentState(States.PROGRAM_LOADED_NOT_AUTOSTEPPING);
		model.getCurrentState().enter();
		setChanged();
		notifyObservers(string);
	}

	public MachineModel getModel() {
		return model;
	}

	public void setModel(MachineModel model) {
		this.model = model;
	}

	public States getCurrentState(){
		return model.getCurrentState();
	}

	public void setCurrentState(States s){
		if(s == States.PROGRAM_HALTED){
			stepControl.setAutoStepOn(false);
		}
		model.setCurrentState(s);
		//3 Notify Lines
		model.getCurrentState().enter();
		setChanged();
		notifyObservers();
	}

	public void exit() { // method executed when user exits the program
		int decision = JOptionPane.showConfirmDialog(
				frame, "Do you really wish to exit?",
				"Confirmation", JOptionPane.YES_NO_OPTION);
		if (decision == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
}
