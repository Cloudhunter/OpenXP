package openxp.common.core;

import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import openxp.OpenXP;

public class XPTank extends LiquidTank {

	public XPTank(int capacity) {
		super(capacity);
	}
	
    @Override
    public int fill(LiquidStack resource, boolean doFill)
    {
    	if (!isValidResource(resource)) {
    		return 0;
    	}
    	return super.fill(resource, doFill);
    }
    
    public boolean isValidResource(LiquidStack resource) {
    	return resource != null && resource.itemID == OpenXP.Items.liquidXP.itemID;
    }

}
