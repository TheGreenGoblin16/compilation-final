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
	private static final int WORD_SIZE=4;
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
	public void Exit()
	{
		fileWriter.print("\tli $v0,10\n");
		fileWriter.print("\tsyscall\n");
	}
	public void printInt(Temp t)
	{
		int idx=t.getSerialNumber();
		// fileWriter.format("\taddi $a0,Temp_%d,0\n",idx);
		fileWriter.format("\tmove $a0,Temp_%d\n",idx);
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
	public void load(Temp dst, String varName)
	{
		int idxdst=dst.getRegIndex();
		fileWriter.format("\tlw $t%d,global_%s\n",idxdst,varName);
	}

	//lw $t0 , 4($s0)
	public void load(String dst, int offset , String src)
	{
		fileWriter.format("\tlw %s , %d(%s)\n",dst,offset,src);
	}

	public void loadString(Temp dst, String varName)
	{
		int idxdst = dst.getRegIndex();
		fileWriter.format("\tla $t%d, %s\n",idxdst,varName);
	}
	public void store(String varName, Temp src)
	{
		int idxsrc=src.getSerialNumber();
		fileWriter.format("\tsw Temp_%d, global_%s\n",idxsrc,varName);
	}

	// sw src offset(dst)
	public void store(String src, int offset , String dst)
	{
		fileWriter.format("\tsw %s, %d(%s)\n",src, offset ,dst);
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
	public void beq(Temp oprnd1, Temp oprnd2, String label)
	{
		int i1 =oprnd1.getRegIndex();
		int i2 =oprnd2.getRegIndex();
		
		fileWriter.format("\tbeq $t%d, $t%d, %s\n",i1,i2,label);				
	}
	public void beqz(Temp oprnd1, String label)
	{
		int i1 =oprnd1.getRegIndex();
				
		fileWriter.format("\tbeq $t%d, $zero, %s\n",i1,label);
	}
	public void bnez(Temp oprnd1, String label)
	{
		int i1 =oprnd1.getRegIndex();
				
		fileWriter.format("\tbne $t%d, $zero, %s\n",i1,label);
	}

	public void move(Temp dst , Temp src){
		int i1 = dst.getRegIndex();
		int i2 = src.getRegIndex();

		fileWriter.format("\t move $t%d , $t%d \n" , i1 , i2);
	}

	public void move(String dst , String src){
		fileWriter.format("\t move %s , %s \n" , dst , src);
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