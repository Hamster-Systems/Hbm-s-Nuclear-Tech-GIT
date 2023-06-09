package com.hbm.render.tileentity;

import org.lwjgl.opengl.GL11;

import com.hbm.main.ResourceManager;
import com.hbm.tileentity.turret.TileEntityTurretCIWS;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class RenderCIWSTurret extends TileEntitySpecialRenderer<TileEntityTurretCIWS> {

	@Override
	public void render(TileEntityTurretCIWS te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5D, y, z + 0.5D);
        GlStateManager.enableLighting();
        GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glRotatef(180, 0F, 1F, 0F);
		
		double yaw = te.rotationYaw;
		double pitch = te.rotationPitch;
		
        
		this.bindTexture(ResourceManager.turret_ciws_base_tex);
        ResourceManager.turret_cwis_base.renderAll();

        GL11.glPopMatrix();
        
        renderTileEntityAt2(te, x, y, z, partialTicks, yaw, pitch);
	}
	
	public void renderTileEntityAt2(TileEntity tileEntity, double x, double y, double z, float f, double yaw, double pitch)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5D, y, z + 0.5D);
        GlStateManager.enableLighting();
        GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glRotatef(180, 0F, 1F, 0F);

		GL11.glRotated(yaw + 180, 0F, -1F, 0F);

		this.bindTexture(ResourceManager.turret_ciws_rotor_tex);
        ResourceManager.turret_cwis_rotor.renderAll();

        GL11.glPopMatrix();
        
        renderTileEntityAt3(tileEntity, x, y, z, f, yaw, pitch);
    }
    
	public void renderTileEntityAt3(TileEntity tileEntity, double x, double y, double z, float f, double yaw, double pitch)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5D, y + 1.5D, z + 0.5D);
        GlStateManager.enableLighting();
        GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glRotatef(180, 0F, 1F, 0F);

		GL11.glRotated(yaw + 180, 0F, -1F, 0F);
		GL11.glRotated(pitch, 1F, 0F, 0F);

		this.bindTexture(ResourceManager.turret_ciws_head_tex);
        ResourceManager.turret_cwis_head.renderAll();

        GL11.glPopMatrix();
        
        renderTileEntityAt4(tileEntity, x, y, z, f, yaw, pitch);
    }
	
	public void renderTileEntityAt4(TileEntity tileEntity, double x, double y, double z, float f, double yaw, double pitch)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5D, y + 1.5D, z + 0.5D);
        GlStateManager.enableLighting();
        GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glRotatef(180, 0F, 1F, 0F);
        
		GL11.glRotated(yaw + 180, 0F, -1F, 0F);
		GL11.glRotated(pitch, 1F, 0F, 0F);
		GL11.glRotated(((TileEntityTurretCIWS)tileEntity).rotation, 0F, 0F, 1F);

		this.bindTexture(ResourceManager.turret_ciws_gun_tex);
        ResourceManager.turret_cwis_gun.renderAll();

        GL11.glPopMatrix();
    }
}
