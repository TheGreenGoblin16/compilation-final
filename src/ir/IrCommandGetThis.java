package ir;

import mips.MipsGenerator;
import temp.*;

public class IrCommandGetThis extends IrCommand{

    public Temp dst;

    public IrCommandGetThis(Temp dst){
        this.dst = dst;
    }

    public void printMe() {
        System.out.println("IrCommandGetThis");
        System.out.println("dst: " + dst);
    }

    public void mipsMe(){
        // "this" is the first param
        int thisIndexFromFp = (2+0)*4;
        MipsGenerator.getInstance().load(dst.toString() , thisIndexFromFp , "$fp");
    }
}
