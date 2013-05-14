package openxp.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelXPSponge extends ModelBase {

	ModelRenderer main;
	float f5 = 0.0625F;

	public ModelXPSponge() {
		textureWidth = 128;
		textureHeight = 128;

		main = new ModelRenderer(this, 0, 0);
		main.addBox(-8F, 0F, -8F, 16, 16, 16);
		main.setRotationPoint(0F, 0F, 0F);
		main.setTextureSize(128, 128);
		main.mirror = true;
		setRotation(main, 0F, 0F, 0F);
		
	}

	public void render() {
		main.render(f5);
	}


	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
