package openxp.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelLifeStone extends ModelBase {

	ModelRenderer main;

	float f5 = 0.0625F;

	public ModelLifeStone() {
		textureWidth = 16;
		textureHeight = 16;

		main = new ModelRenderer(this, 0, 0);
		main.addBox(-2F, 0F, -2F, 4, 7, 4);
		main.setRotationPoint(0F, 9F, 0F);
		main.setTextureSize(16, 16);
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