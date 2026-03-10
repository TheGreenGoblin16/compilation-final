/***********/
/* PACKAGE */
/***********/
package mips;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import temp.*;
import ir.*;

public class MipsGenerator
{
	public static final int WORD_SIZE=4; // was private but i need it to be public so...
	/***********************/
	/* The file writer ... */
	/***********************/
	private PrintWriter fileWriter;

	/***********************/
	/* The file writer ... */
	/***********************/
	public void finalizeFile()
	{
		fileWriter.print("\tli $v0,10\n");
		fileWriter.print("\tsyscall\n");
		fileWriter.close();
	}
	public void ExitAsm()
	{
		fileWriter.print("\tli $v0,10\n");
		fileWriter.print("\tsyscall\n");
	}
	public void printInt(Temp t)
	{
		int idx=t.getRegIndex();
		// fileWriter.format("\taddi $a0,Temp_%d,0\n",idx);
		fileWriter.format("\tmove $a0, $t%d\n",idx);
		fileWriter.format("\tli $v0,1\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tli $a0,32\n");
		fileWriter.format("\tli $v0,11\n");
		fileWriter.format("\tsyscall\n");
	}
	public void printString(String label)
	{
		fileWriter.format("\tla $a0, %s\n",label);
		fileWriter.format("\tli $v0,4\n");
		fileWriter.format("\tsyscall\n");
	}
	public void printString(Temp t)
	{
		int idx=t.getRegIndex();

		fileWriter.format("\tmove $a0, $t%d\n",idx);
		fileWriter.format("\tli $v0,4\n");
		fileWriter.format("\tsyscall\n");
	}
//	public Temp addressLocalVar(int serialLocalVarNum)
//	{
//		Temp t  = TempFactory.getInstance().getFreshTemp();
//		int idx = t.getSerialNumber();
//
//		fileWriter.format("\taddi Temp_%d,$fp,%d\n",idx,-serialLocalVarNum*WORD_SIZE);
//
//		return t;
//	}
	public void allocate(String varName)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\tglobal_%s: .word 721\n",varName);
	}
	public void allocate(Temp t, String reg_value) // reg_value is a register for the value.
	{
		int idx = t.getRegIndex();
		fileWriter.format("\tli $v0, 9\n");
		fileWriter.format("\tmove $a0, %s\n", reg_value);
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tmove $t%d, $v0\n", idx);
	}
	public void load(Temp dst, String varName)
	{
		int idxdst=dst.getRegIndex();
		fileWriter.format("\tlw $t%d,%s\n",idxdst,varName);
	}

	//lw $t0 , 4($s0)
	public void load(String dst, int offset, String src)
	{
		fileWriter.format("\tlw %s, %d(%s)\n",dst,offset,src);
	}

	public void la(Temp dst, String varName)
	{
		int idxdst = dst.getRegIndex();
		fileWriter.format("\tla $t%d, %s\n",idxdst,varName);
	}
	public void la(String dst, String label)
	{
		fileWriter.format("\tla %s, %s\n",dst,label);
	}

	public void sw(Temp src , String label)
	{
		int idxsrc=src.getRegIndex();
		fileWriter.format("\tsw $t%d, %s\n",idxsrc,label);
	}

	// sw src offset(dst)
	public void sw(String src, int offset , String dst)
	{
		fileWriter.format("\tsw %s, %d(%s)\n",src, offset ,dst);
	}

	public void sw(Temp src, int offset , Temp dst)
	{
		int idxsrc = src.getRegIndex();
		int idxdst = dst.getRegIndex();
		fileWriter.format("\tsw $t%d, %d( $t%d )\n",idxsrc, offset ,idxdst);
	}

	public void sb(String src, int offset , String dst)
	{
		fileWriter.format("\tsb %s, %d(%s)\n",src, offset ,dst);
	}

	public void li(Temp t, int value)
	{
		int idx=t.getRegIndex();
		fileWriter.format("\tli $t%d,%d\n",idx,value);
	}
	// li src, value
	public void li(String src, int value)
	{
		fileWriter.format("\tli %s,%d\n",src,value);
	}

	public void lb(String src, int offset, String dst)
	{
		fileWriter.format("\tlb %s,%d(%s)\n",src,offset,dst);
	}
	public void add(Temp dst, Temp oprnd1, Temp oprnd2)
	{
		int i1 =oprnd1.getRegIndex();
		int i2 =oprnd2.getRegIndex();
		int dstidx=dst.getRegIndex();

		fileWriter.format("\tadd $t%d, $t%d, $t%d\n",dstidx,i1,i2);
	}

	public void add(String dst, String oprnd1, String oprnd2)
	{
		fileWriter.format("\tadd %s, %s, %s \n",dst,oprnd1 , oprnd2);
	}

	public void addu(String dst, String oprnd1, String oprnd2)
	{
		fileWriter.format("\tadd %s, %s, %s \n",dst,oprnd1 , oprnd2);
	}

	public void addi(Temp dst, Temp oprnd1, int num)
	{
		int i1 =oprnd1.getRegIndex();
		int dstidx=dst.getRegIndex();

		fileWriter.format("\taddi $t%d, $t%d, %d\n",dstidx,i1,num);
	}

	public void addi(String dst, String oprnd1, int num)
	{

		fileWriter.format("\taddi %s, %s, %d\n",dst , oprnd1 ,num);
	}

	public void sub(Temp dst, Temp oprnd1, Temp oprnd2)
	{
		int i1 =oprnd1.getRegIndex();
		int i2 =oprnd2.getRegIndex();
		int dstidx=dst.getRegIndex();

		fileWriter.format("\tsub $t%d, $t%d, $t%d\n",dstidx,i1,i2);
	}
	public void mul(Temp dst, Temp oprnd1, Temp oprnd2)
	{
		int i1 =oprnd1.getRegIndex();
		int i2 =oprnd2.getRegIndex();
		int dstidx=dst.getRegIndex();

		fileWriter.format("\tmul $t%d, $t%d, $t%d\n",dstidx,i1,i2);
	}

	public void muli(Temp dst, Temp oprnd1, int num)
	{
		int i1 =oprnd1.getRegIndex();
		int dstidx=dst.getRegIndex();

		fileWriter.format("\tmul $t%d, $t%d, %d\n",dstidx,i1,num);
	}

	public void muli(String dst, String oprnd1, int num)
	{
		fileWriter.format("\tmul %s, %s, %d\n",dst , oprnd1 ,num);
	}

	public void div(Temp dst, Temp oprnd1, Temp oprnd2)
	{
		int i1 =oprnd1.getRegIndex();
		int i2 =oprnd2.getRegIndex();
		int dstidx=dst.getRegIndex();

		fileWriter.format("\tdiv $t%d, $t%d, $t%d\n",dstidx,i1,i2);
	}
	public void label(String inlabel)
	{
		if (inlabel.equals("main"))
		{
			fileWriter.format(".text\n");
			fileWriter.format("%s:\n",inlabel);
		}
		else
		{
			fileWriter.format("%s:\n",inlabel);
		}
	}
	public void jump(String inlabel)
	{
		fileWriter.format("\tj %s\n",inlabel);
	}	
	public void blt(Temp oprnd1, Temp oprnd2, String label)
	{
		int i1 =oprnd1.getRegIndex();
		int i2 =oprnd2.getRegIndex();
		
		fileWriter.format("\tblt $t%d, $t%d, %s\n",i1,i2,label);				
	}
	public void bltz(Temp oprnd1, String label)
	{
		int i1 =oprnd1.getRegIndex();
		
		fileWriter.format("\tblt $t%d, $zero, %s\n",i1,label);
	}
	public void bgez(Temp oprnd1, String label)
	{
		int i1 =oprnd1.getRegIndex();
		
		fileWriter.format("\tbge $t%d, $zero, %s\n",i1,label);
	}
	public void ble(Temp oprnd1, String src, String label)
	{
		int i1 =oprnd1.getRegIndex();
		
		fileWriter.format("\tble $t%d, %s, %s\n",i1,src,label);
	}
	public void bge(Temp oprnd1, Temp oprnd2, String label)
	{
		int i1 =oprnd1.getRegIndex();
		int i2 =oprnd2.getRegIndex();
		
		fileWriter.format("\tbge $t%d, $t%d, %s\n",i1,i2,label);				
	}

	public void bge(Temp oprnd1, String oprnd2, String label)
	{
		int i1 =oprnd1.getRegIndex();

		fileWriter.format("\tbge $t%d, %s, %s\n",i1,oprnd2,label);
	}

	public void bge(String oprnd1, String oprnd2, String label)
	{
		fileWriter.format("\tbge %s, %s, %s\n",oprnd1,oprnd2,label);
	}

	public void ble(Temp oprnd1, Temp oprnd2, String label)
	{
		int i1 =oprnd1.getRegIndex();
		int i2 =oprnd2.getRegIndex();
		
		fileWriter.format("\tble $t%d, $t%d, %s\n",i1,i2,label);	
	}
	public void bne(Temp oprnd1, Temp oprnd2, String label)
	{
		int i1 =oprnd1.getRegIndex();
		int i2 =oprnd2.getRegIndex();
		
		fileWriter.format("\tbne $t%d, $t%d, %s\n",i1,i2,label);				
	}
	public void bne(String oprnd1, String oprnd2, String label)
	{
		fileWriter.format("\tbne %s, %s, %s\n",oprnd1,oprnd2,label);				
	}

	public void beq(Temp oprnd1, Temp oprnd2, String label)
	{
		int i1 =oprnd1.getRegIndex();
		int i2 =oprnd2.getRegIndex();
		
		fileWriter.format("\tbeq $t%d, $t%d, %s\n",i1,i2,label);				
	}
	public void beq(Temp oprnd1, String oprnd2, String label)
	{
		int i1 =oprnd1.getRegIndex();
		
		fileWriter.format("\tbeq $t%d, %s, %s\n",i1,oprnd2,label);				
	}
	public void beqz(Temp oprnd1, String label)
	{
		int i1 =oprnd1.getRegIndex();
				
		fileWriter.format("\tbeq $t%d, $zero, %s\n",i1,label);
	}
	public void beqz(String oprnd1, String label)
	{
		fileWriter.format("\tbeq %s, $zero, %s\n",oprnd1,label);
	}
	public void bnez(Temp oprnd1, String label)
	{
		int i1 =oprnd1.getRegIndex();
				
		fileWriter.format("\tbne $t%d, $zero, %s\n",i1,label);
	}
	public void bnez(String oprnd1, String label)
	{				
		fileWriter.format("\tbne $t%d, $zero, %s\n",oprnd1,label);
	}

	public void move(Temp dst , Temp src){
		int i1 = dst.getRegIndex();
		int i2 = src.getRegIndex();

		fileWriter.format("\tmove $t%d, $t%d \n" , i1 , i2);
	}

	public void move(Temp dst, String src){
		int i1 = dst.getRegIndex();

		fileWriter.format("\tmove $t%d, %s \n",i1, src);
	}
	public void move(String dst, Temp src){
		int i1 = src.getRegIndex();

		fileWriter.format("\tmove %s, $t%d\n",dst,i1);
	}

	public void move(String dst, String src){
		fileWriter.format("\tmove %s, %s \n" , dst , src);
	}

	public void jal(String label)
	{
		fileWriter.format("\tjal %s\n",label);
	}

	public void jalr(String reg)
	{
		fileWriter.format("\tjalr %s\n",reg);
	}

	public void jr(String reg)
	{
		fileWriter.format("\tjr %s\n",reg);
	}

	public void push(Temp src)
	{
		int idx = src.getRegIndex();
		fileWriter.format("\taddi $sp, $sp, -%d\n",WORD_SIZE);
		fileWriter.format("\tsw $t%d, 0($sp)\n",idx);
	}
	public void push(String src)
	{
		fileWriter.format("\taddi $sp, $sp, -%d\n",WORD_SIZE);
		fileWriter.format("\tsw %s, 0($sp)\n",src);
	}

	public void pop(Temp dst)
	{
		int idx = dst.getRegIndex();
		fileWriter.format("\tlw $t%d, 0($sp)\n",idx);
		fileWriter.format("\taddi $sp, $sp, %d\n",WORD_SIZE);
	}
	public void pop(String dst)
	{
		fileWriter.format("\tlw %s, 0($sp)\n",dst);
		fileWriter.format("\taddi $sp, $sp, %d\n",WORD_SIZE);
	}
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static MipsGenerator instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected MipsGenerator() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static MipsGenerator getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new MipsGenerator();

			try
			{
				/*********************************************************************************/
				/* [1] Open the MIPS text file and write data section with error message strings */
				/*********************************************************************************/
				String dirname="./output/";
				String filename=String.format("MIPS.txt");

				/***************************************/
				/* [2] Open MIPS text file for writing */
				/***************************************/
				instance.fileWriter = new PrintWriter(dirname+filename);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			/*****************************************************/
			/* [3] Print data section with error message strings */
			/*****************************************************/
			instance.fileWriter.print(".data\n");
			instance.fileWriter.print("\tstring_access_violation: .asciiz \"Access Violation\"\n");
			instance.fileWriter.print("\tstring_illegal_div_by_0: .asciiz \"Illegal Division By Zero\"\n");
			instance.fileWriter.print("\tstring_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");
			instance.fileWriter.print(Ir.getInstance().dataSegment);
			
		}
		return instance;
	}
}