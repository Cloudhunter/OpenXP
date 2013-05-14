package openxp.client.renderer;

import java.util.Random;

import openxp.OpenXP;
import openxp.client.model.ModelXPSponge;
import openxp.common.tileentity.TileEntityXPIngester;
import openxp.common.tileentity.TileEntityXPSponge;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.LiquidStack;

public class RendererXPSponge extends TileEntitySpecialRenderer {

	protected ModelXPSponge model = new ModelXPSponge();
	RenderItem renderItem;
	protected ItemStack emptyBottleStack;
	protected ItemStack xpBottleStack;
	
	protected Random rnd = new Random();
	
	protected orb[] orbs;

    RenderBlocks renderBlocks = new RenderBlocks();
    
	public class orb {
		public double x = rnd.nextDouble()*0.6;
		public double y = 0;
		public double z = rnd.nextDouble()*0.6;
		public int tex = rnd.nextInt(11);
		public int color = 0;
		
	}
	
	public RendererXPSponge() {
		orbs = new orb[10];
		for (int i=0; i < orbs.length; i++) {
			orbs[i] = new orb();
		}
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z,
			float partialTick) {
		
		TileEntityXPSponge sponge = (TileEntityXPSponge) tileentity;
		
		double top = 0.05 + (sponge.getPercentFull() * 0.9);
		
		if (sponge.getPercentFull() > 0.1) {
			for (int i=0; i<orbs.length;i++) {
				orbs[i].y = top-0.05;
				renderOrbs(x, y, z, orbs[i]);
			}
		}

		GL11.glPushMatrix();
	    
			GL11.glTranslatef((float)x + 0.5F, (float)y, (float)z + 0.5F);

			GL11.glPushMatrix();

				LiquidStack stack = sponge.getLiquidStack();
				if (stack != null) {

				    GL11.glDisable(2896);
				    Tessellator t = Tessellator.instance;
				    renderBlocks.setRenderBounds(0.05D, 0.05D, 0.05D, 0.95D, top, 0.95D);
				    t.startDrawingQuads();

				    t.setColorOpaque_F(1.0F, 1.0F, 1.0F);
				    t.setBrightness(200);

					bindTextureByName("/mods/openxp/textures/models/xpjuice.png");
				    renderBlocks.renderFaceXNeg(OpenXP.Blocks.XPSponge,-0.5D, 0.0D, -0.5D, OpenXP.Blocks.XPSponge.iconLiquid);
				    renderBlocks.renderFaceXPos(OpenXP.Blocks.XPSponge,-0.5D, 0.0D, -0.5D, OpenXP.Blocks.XPSponge.iconLiquid);
				    renderBlocks.renderFaceYNeg(OpenXP.Blocks.XPSponge,-0.5D, 0.0D, -0.5D, OpenXP.Blocks.XPSponge.iconLiquid);
				    renderBlocks.renderFaceYPos(OpenXP.Blocks.XPSponge,-0.5D, 0.0D, -0.5D, OpenXP.Blocks.XPSponge.iconLiquid);
				    renderBlocks.renderFaceZNeg(OpenXP.Blocks.XPSponge,-0.5D, 0.0D, -0.5D, OpenXP.Blocks.XPSponge.iconLiquid);
				    renderBlocks.renderFaceZPos(OpenXP.Blocks.XPSponge,-0.5D, 0.0D, -0.5D, OpenXP.Blocks.XPSponge.iconLiquid);
				    
				    t.draw();

				    GL11.glEnable(2896);
				}

				
				bindTextureByName("/mods/openxp/textures/models/xpingester.png");
				
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, 1);
				model.render();
				GL11.glDisable(GL11.GL_BLEND);

				
			GL11.glPopMatrix();
			
			GL11.glScalef(0.02f, 0.02f, 0.02f);
			GL11.glRotatef(180.0F, 0, 0, 1);
			
			renderItem = ((RenderItem) RenderManager.instance.getEntityClassRenderObject(EntityItem.class));
		    RenderEngine re = Minecraft.getMinecraft().renderEngine;
		    FontRenderer fr = getFontRenderer();
		    
		    /*
		    if (ingester.hasInputStack()) {
		    	renderItem.renderItemIntoGUI(fr, re, emptyBottleStack, -28, 31);
				renderItem.renderItemIntoGUI(fr, re, emptyBottleStack, -8, 31);
		    }
		    if (ingester.hasOutputStack()) {
		    	renderItem.renderItemIntoGUI(fr, re, xpBottleStack, 11, 31);
		    }
*/
			//GL11.glRotatef(180.0F, 0, 1, 0);
			//renderItem.renderItemIntoGUI(fr, re, stack, -19, 0);
		    
		GL11.glPopMatrix();
		
		
	}
	
	private void renderOrbs(double x, double y, double z, orb o) {

        GL11.glPushMatrix();
        GL11.glTranslated(x+o.x+0.2, y+o.y, z+o.z+0.2);
        int i = o.tex;
        bindTextureByName("/item/xporb.png");
        Tessellator tessellator = Tessellator.instance;
        float f2 = (float)(i % 4 * 16 + 0) / 64.0F;
        float f3 = (float)(i % 4 * 16 + 16) / 64.0F;
        float f4 = (float)(i / 4 * 16 + 0) / 64.0F;
        float f5 = (float)(i / 4 * 16 + 16) / 64.0F;
        float f6 = 1.0F;
        float f7 = 0.5F;
        float f8 = 0.25F;
        int j = 255;
        int k = j % 65536;
        int l = j / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k / 1.0F, (float)l / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f9 = 255.0F;
        if (rnd.nextInt(10) == 1) {
        	o.color++;
        }
        float f10 = ((float)o.color + f9) / 2.0F;
        l = (int)((MathHelper.sin(f10 + 0.0F) + 1.0F) * 0.5F * f9);
        int i1 = (int)f9;
        int j1 = (int)((MathHelper.sin(f10 + 4.1887903F) + 1.0F) * 0.1F * f9);
        int k1 = l << 16 | i1 << 8 | j1;
        GL11.glRotatef(180.0F - RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
        float f11 = 0.2F;
        GL11.glScalef(f11, f11, f11);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(k1, 128);
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertexWithUV((double)(0.0F - f7), (double)(0.0F - f8), 0.0D, (double)f2, (double)f5);
        tessellator.addVertexWithUV((double)(f6 - f7), (double)(0.0F - f8), 0.0D, (double)f3, (double)f5);
        tessellator.addVertexWithUV((double)(f6 - f7), (double)(1.0F - f8), 0.0D, (double)f3, (double)f4);
        tessellator.addVertexWithUV((double)(0.0F - f7), (double)(1.0F - f8), 0.0D, (double)f2, (double)f4);
        tessellator.draw();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
	}

}
