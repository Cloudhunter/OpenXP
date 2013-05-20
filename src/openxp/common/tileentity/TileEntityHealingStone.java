package openxp.common.tileentity;

import java.util.List;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import openxp.common.core.BaseTileEntity;

public class TileEntityHealingStone extends BaseTileEntity {

	public Vec3 healingLocation;
	public String target = null;
	private AxisAlignedBB playerBounds;
	private Vec3 tilePosition;
	
	public TileEntityHealingStone() {
	}
	
	@Override
	public void initialize() {
		healingLocation = Vec3.createVectorHelper(xCoord, yCoord, zCoord);
		playerBounds = AxisAlignedBB.getBoundingBox(
				xCoord - 30,
				yCoord - 30,
				zCoord - 30,
				xCoord + 30,
				yCoord + 30,
				zCoord + 31
		);
		tilePosition = Vec3.createVectorHelper(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		EntityPlayer player = null;
		double distanceToPlayer = 0;
		double distanceToSource = healingLocation.distanceTo(tilePosition);
		Vec3 targetLocation = null;
		
		if (target != null) {
			player = worldObj.getPlayerEntityByName(target);
			if (player != null) {
				if (player.getDistance(xCoord, yCoord, zCoord) < 30.0) {
					targetLocation = player.getPosition(0);
					distanceToPlayer = healingLocation.distanceTo(targetLocation);
				}else {
					player = null;
					setTarget(null);
				}
			}
		}
		if (targetLocation == null) {
			targetLocation = tilePosition;
		}
		
		if (player != null) {
			if (distanceToPlayer < 2.0 && !worldObj.isRemote && worldObj.rand.nextDouble() < 0.3) {
				player.heal(1);
			}
			if (player.getHealth() >= player.getMaxHealth()) {
				player = null;
				setTarget(null);
			}
		}
		
		Vec3 v = healingLocation.subtract(targetLocation);
		Vec3 moveDir = v.normalize();
		healingLocation.xCoord += moveDir.xCoord * 0.2;
		healingLocation.zCoord += moveDir.zCoord * 0.2;
		healingLocation.yCoord += moveDir.yCoord * 0.2;
		if (!worldObj.isRemote && player == null) {
			target = null;
			List<EntityPlayer> players = (List<EntityPlayer>) worldObj.getEntitiesWithinAABB(EntityPlayer.class, playerBounds);
			int checked = 0;
			while (checked < players.size()) {
				int rand = worldObj.rand.nextInt(players.size());
				EntityPlayer playerToCheck = players.get(rand);
				if (tilePosition.distanceTo(playerToCheck.getPosition(0)) <= 30.0 && playerToCheck.getHealth() < playerToCheck.getMaxHealth()) {
					setTarget(playerToCheck.username);
					break;
				}else {
					players.remove(playerToCheck);
				}
				checked++;
			}
			if (target == null) {
				setTarget(null);
			}
		}
		
		if (worldObj.isRemote){
			if (distanceToSource > 1.0) {
				String particle = "spell";
				if (target != null) {
					particle = "heart";
				}
				worldObj.spawnParticle(
						particle,
						healingLocation.xCoord + (worldObj.rand.nextDouble() * 2 - 1),
						healingLocation.yCoord + (worldObj.rand.nextDouble() * 2 - 1),
						healingLocation.zCoord + (worldObj.rand.nextDouble() * 2 - 1),
						0.0D, 0.0D, 0.0D);
			}
			
		}
	}
	
	private void setTarget(String newtarget) {
		String oldtarget = target;
		this.target = newtarget;
		if (!worldObj.isRemote && oldtarget != newtarget) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if (tag.hasKey("target")) {
			target = tag.getString("target");
		}else {
			target = null;
		}
		if (tag.hasKey("tpX")) {
			tilePosition = Vec3.createVectorHelper(
					tag.getDouble("tpX"), 
					tag.getDouble("tpY"),
					tag.getDouble("tpZ"));
		}
		
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		if (target != null) {
			tag.setString("target", target);
		}
		if (tilePosition != null) {
			tag.setDouble("tpX", tilePosition.xCoord);
			tag.setDouble("tpY", tilePosition.yCoord);
			tag.setDouble("tpZ", tilePosition.zCoord);
		}
	}
}
