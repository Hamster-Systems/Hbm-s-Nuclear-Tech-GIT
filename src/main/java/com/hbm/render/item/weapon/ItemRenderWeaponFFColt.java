package com.hbm.render.item.weapon;

import org.lwjgl.opengl.GL11;

import com.hbm.main.ResourceManager;
import com.hbm.render.item.TEISRBase;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ItemRenderWeaponFFColt extends TEISRBase {

	ResourceLocation main;
	ResourceLocation hammer;
	ResourceLocation grip;

	public ItemRenderWeaponFFColt(ResourceLocation main, ResourceLocation hammer, ResourceLocation grip) {
		this.main = main;
		this.hammer = hammer;
		this.grip = grip;
	}

	@Override
	public void renderByItem(ItemStack item) {
		if(type == null) return;
		GL11.glPushMatrix();
		GlStateManager.enableCull();
		double s0 = 1.5D;
		double s1 = 1.5D;
		double s2 = 1.5D;
		switch(type) {

		case FIRST_PERSON_LEFT_HAND:

			GL11.glTranslated(1, 0.5, 0.3);
			GL11.glScaled(s0, s0, s0);
			GL11.glRotated(100, 0, -1, 0);
			GL11.glRotated(25, 1, 0, 0);

			break;

		case FIRST_PERSON_RIGHT_HAND:

			GL11.glTranslated(0, 0.5, 0.3);
			GL11.glScaled(s0, s0, s0);
			GL11.glRotated(100, 0, 1, 0);
			GL11.glRotated(25, 1, 0, 0);

			break;

		case THIRD_PERSON_LEFT_HAND:
		case THIRD_PERSON_RIGHT_HAND:

			GL11.glRotated(5, 1, 0, 0);
			GL11.glTranslated(0.5, 0.1, -0.25);
			GL11.glScaled(s1, s1, s1);

			break;

		case HEAD:
		case FIXED:
		case GROUND:

			GL11.glRotated(90, 0, -1, 0);
			GL11.glScaled(s2, s2, s2);

			break;
		case GUI:

			GlStateManager.enableLighting();
			GL11.glTranslated(0.5, 0.5, 0);
			GL11.glRotated(-45, 0, 0, 1);
			GL11.glRotated(90, 0, 1, 0);
			break;

		default: break;
		}

		Minecraft.getMinecraft().renderEngine.bindTexture(main);
		ResourceManager.ff_python.renderPart("Body");
		ResourceManager.ff_python.renderPart("Cylinder");
		Minecraft.getMinecraft().renderEngine.bindTexture(grip);
		ResourceManager.ff_python.renderPart("Grip");
		Minecraft.getMinecraft().renderEngine.bindTexture(hammer);
		ResourceManager.ff_python.renderPart("Hammer");

		GL11.glPopMatrix();
	}
}