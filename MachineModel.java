package project;

import java.util.Map;
import java.util.TreeMap;

public class MachineModel {
	public final Map<Integer, Instruction> IMAP = new TreeMap<>();
	private CPU cpu = new CPU();
	private Memory memory = new Memory();
	private HaltCallback callback;

	public MachineModel(HaltCallback callBack){
		//NOP
		IMAP.put(0x0, (arg, level) -> {
			cpu.incrPC();
		});
		//LOD
		IMAP.put(0x1, (arg, level) -> {
			if(level < 0 || level > 2){
				throw new IllegalArgumentException("Illegal indirection level in LOD instruction.");
			}
			if(level>0){
				IMAP.get(0x1).execute(memory.getData(cpu.getMemBase()), level-1);
			}
			else{
				cpu.setAccum(arg);
				cpu.incrPC();
			}
		});
		//STO
		IMAP.put(0x2, (arg, level) -> {
			if(level < 1 || level > 2){
				throw new IllegalArgumentException("Illegal indirection level in STO instruction.");
			}
			if(level == 1){
				memory.setData(arg,  cpu.getAccum());
				cpu.incrPC();
			}
			else {
				IMAP.get(0x2).execute(memory.getData(cpu.getMemBase()), level-1);
			}
		});
		//ADD
		IMAP.put(0x3, (arg, level) -> {
			if(level < 0 || level > 2) {
				throw new IllegalArgumentException(
						"Illegal indirection level in ADD instruction");
			}
			if(level > 0) {
				IMAP.get(0x3).execute(memory.getData(cpu.getMemBase()+arg), level-1);
			} else {
				cpu.setAccum(cpu.getAccum() + arg);
				cpu.incrPC();
			}
		});
		//SUB
		IMAP.put(0x4, (arg, level) -> {
			if(level < 0 || level > 2) {
				throw new IllegalArgumentException(
						"Illegal indirection level in SUB instruction");
			}
			if(level > 0) {
				IMAP.get(0x3).execute(memory.getData(cpu.getMemBase()+arg), level-1);
			} else {
				cpu.setAccum(cpu.getAccum() - arg);
				cpu.incrPC();
			}
		});
		//MUL
		IMAP.put(0x5, (arg, level) ->{
			if(level < 0 || level > 2) {
				throw new IllegalArgumentException(
						"Illegal indirection level in MUL instruction.");}
			if(level > 0) {
				IMAP.get(0x3).execute(memory.getData(cpu.getMemBase()+arg), level-1);
			} else {
				cpu.setAccum(cpu.getAccum() * arg);
				cpu.incrPC();}});

		//DIV
		IMAP.put(0x6, (arg, level) ->{
			if(level < 0 || level > 2) {
				throw new IllegalArgumentException(
						"Illegal indirection level in DIV instruction.");}
			if(level > 0) {
				IMAP.get(0x3).execute(memory.getData(cpu.getMemBase()+arg), level-1);} 
			if(arg==0)
				throw new DivideByZeroException("Can't divide by zero.");
			else {
				cpu.setAccum(cpu.getAccum() / arg);
				cpu.incrPC();}});
		//AND
		IMAP.put(0x7, (arg, level) -> {
			if(level < 0 || level > 1){
				throw new IllegalArgumentException("Illegal indirection level in AND instruction.");
			}
			if (level > 0) {
				IMAP.get(0x7).execute(memory.getData(arg), level-1);
			} else {
				if (arg != 0 && cpu.getAccum() != 0) {
					cpu.setAccum(1);
				}
				else {
					cpu.setAccum(0);
				}
				cpu.incrPC();
			}
		});
		//NOT
		IMAP.put(0x8, (arg,level) -> {
			if(cpu.getAccum() == 0){
				cpu.setAccum(1);
			}
			else {
				cpu.setAccum(0);
			}
			cpu.incrPC();
		});
		//CMPL
		IMAP.put(0xA, (arg,level) -> {
			if(level != 1) {
				throw new IllegalArgumentException(
						"Illegal indirection level in CMPL instruction.");
			}
			if(memory.getData(arg) < 0){
				cpu.setAccum(1);
			}
			else{
				cpu.setAccum(0);
			}
			cpu.incrPC();
		});
		//CMPZ
		IMAP.put(0xA, (arg,level) -> {
			if(level != 1) {
				throw new IllegalArgumentException(
						"Illegal indirection level in CMPZ instruction.");
			}
			if(memory.getData(arg) == 0){
				cpu.setAccum(1);
			}
			else{
				cpu.setAccum(0);
			}
			cpu.incrPC();
		});
		//JUMP
		IMAP.put(0xB, (arg,level) -> {
			if(level < 0 || level > 1) {
				throw new IllegalArgumentException(
						"Illegal indirection level in JUMP instruction.");
			}
			else{
				cpu.setpCounter(cpu.getpCounter()+arg);
			}
		});
		//JMPZ
		IMAP.put(0xB, (arg,level) -> {
			if(level < 0 || level > 1) {
				throw new IllegalArgumentException(
						"Illegal indirection level in JUMP instruction.");
			}
			if(cpu.getAccum()==0){
				cpu.setpCounter(arg);
			}
			else{
				cpu.incrPC();
			}
		});
		//HALT
		IMAP.put(0xF, (arg, level) -> {
			callback.halt();			
		});
	}
	public MachineModel(){
		this(() -> System.exit(0));
	}
	
	public Map<Integer, Instruction> getIMAP() {
		return IMAP;
	}
}
