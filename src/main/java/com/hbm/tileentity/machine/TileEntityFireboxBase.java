package com.hbm.tileentity.machine;

import com.hbm.blocks.BlockDummyable;
import com.hbm.lib.ForgeDirection;
import com.hbm.modules.ModuleBurnTime;
import com.hbm.tileentity.IGUIProvider;
import com.hbm.tileentity.TileEntityMachineBase;

import api.hbm.tile.IHeatSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TileEntityFireboxBase extends TileEntityMachineBase implements ITickable, IGUIProvider, IHeatSource {

	public int maxBurnTime;
	public int burnTime;
	public int burnHeat;
	public boolean wasOn = false;
	public int playersUsing = 0;
	
	public float doorAngle = 0;
	public float prevDoorAngle = 0;

	public int heatEnergy;
    
   
	public TileEntityFireboxBase() {
		super(2);
	    
	}
	 
	@Override
	public void update() {
		
		if(!world.isRemote) {
			
			wasOn = false;
			
			if(burnTime <= 0) {
				
				for(int i = 0; i < 2; i++) {
					if(inventory.getStackInSlot(i) != null) {
						
						int fuel = (int) (getModule().getBurnTime(inventory.getStackInSlot(i)) * getTimeMult());
						
						if(fuel > 0) {
							this.maxBurnTime = this.burnTime = fuel;
							this.burnHeat = getModule().getBurnHeat(getBaseHeat(),inventory.getStackInSlot(i));
							inventory.getStackInSlot(i).shrink(1);

							if(inventory.getStackInSlot(i).getCount() == 0) {
								ItemStack copy = inventory.getStackInSlot(0).copy();
								if(copy.getItem().getContainerItem() != null)
									inventory.setStackInSlot(i, new ItemStack(copy.getItem().getContainerItem()));
							}

							this.wasOn = true;
							break;
						}
					}
				} 
			} else {
				
				if(this.heatEnergy < getMaxHeat()) {
					burnTime--;
				}
				this.wasOn = true;
				
				if(world.rand.nextInt(15) == 0) {
					world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 0.5F + world.rand.nextFloat() * 0.5F);
				}
			}
			
			if(wasOn) {
				this.heatEnergy = Math.min(this.heatEnergy + this.burnHeat, getMaxHeat());
			} else {
				this.heatEnergy = Math.max(this.heatEnergy - Math.max(this.heatEnergy / 1000, 1), 0);
				this.burnHeat = 0;
			}
			
			NBTTagCompound data = new NBTTagCompound();
			data.setInteger("maxBurnTime", this.maxBurnTime);
			data.setInteger("burnTime", this.burnTime);
			data.setInteger("burnHeat", this.burnHeat);
			data.setInteger("heatEnergy", this.heatEnergy);
			data.setInteger("playersUsing", this.playersUsing);
			data.setBoolean("wasOn", this.wasOn);
			this.networkPack(data, 50);
		} else {
			this.prevDoorAngle = this.doorAngle;
			float swingSpeed = (doorAngle / 10F) + 3;
			
			if(this.playersUsing > 0) {
				this.doorAngle += swingSpeed;
			} else {
				this.doorAngle -= swingSpeed;
			}
			
			this.doorAngle = MathHelper.clamp(this.doorAngle, 0F, 135F);
			
			if(wasOn && world.getTotalWorldTime() % 5 == 0) {
				ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDummyable.offset);
				double x = pos.getX() + 0.5 + dir.offsetX;
				double y = pos.getY() + 0.25;
				double z = pos.getZ() + 0.5 + dir.offsetZ;
				world.spawnParticle(EnumParticleTypes.FLAME, wasOn, x + world.rand.nextDouble() * 0.5 - 0.25, y + world.rand.nextDouble() * 0.25, z + world.rand.nextDouble() * 0.5 - 0.25, 0, 0, 0, null);
			}
		}
	}

	public abstract ModuleBurnTime getModule();
	public abstract int getBaseHeat();
	public abstract double getTimeMult();
	public abstract int getMaxHeat();
	
	@Override
	public int[] getAccessibleSlotsFromSide(EnumFacing e) {
		return new int[] { 0, 1 };
	}
	
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemStack) {
		return getModule().getBurnTime(itemStack) > 0;
	}

	@Override
	public void networkUnpack(NBTTagCompound nbt) {
		this.maxBurnTime = nbt.getInteger("maxBurnTime");
		this.burnTime = nbt.getInteger("burnTime");
		this.burnHeat = nbt.getInteger("burnHeat");
		this.heatEnergy = nbt.getInteger("heatEnergy");
		this.playersUsing = nbt.getInteger("playersUsing");
		this.wasOn = nbt.getBoolean("wasOn");
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		this.maxBurnTime = nbt.getInteger("maxBurnTime");
		this.burnTime = nbt.getInteger("burnTime");
		this.burnHeat = nbt.getInteger("burnHeat");
		this.heatEnergy = nbt.getInteger("heatEnergy");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		nbt.setInteger("maxBurnTime", maxBurnTime);
		nbt.setInteger("burnTime", burnTime);
		nbt.setInteger("burnHeat", burnHeat);
		nbt.setInteger("heatEnergy", heatEnergy);
		return super.writeToNBT(nbt);
	}


	public int getHeatStored() {
		return heatEnergy;
	}


	public void useUpHeat(int heat) {
		this.heatEnergy = Math.max(0, this.heatEnergy - heat);
	}
	
	AxisAlignedBB bb = null;
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		
		if(bb == null) {
			bb = new AxisAlignedBB(
					pos.getX() - 1,
					pos.getY(),
					pos.getZ() - 1,
					pos.getX() + 2,
					pos.getY()+ 1,
					pos.getZ() + 2
					);
		}
		
		return bb;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared() {
		return 65536.0D;
	}
}
