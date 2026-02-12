package ir;

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

    public void mipsMe(){}
}
