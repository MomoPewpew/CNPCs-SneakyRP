package noppes.npcs.blocks.tiles;

import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.CustomItems;
import noppes.npcs.EventHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.TextBlock;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.block.ITextPlane;
import noppes.npcs.api.wrapper.BlockScriptedWrapper;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.IScriptBlockHandler;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.entity.data.DataTimers;
import noppes.npcs.util.ValueUtil;

public class TileScripted extends TileNpcEntity implements ITickable, IScriptBlockHandler {
	public List scripts = new ArrayList();
	public String scriptLanguage = "ECMAScript";
	public boolean enabled = false;
	private IBlock blockDummy = null;
	public DataTimers timers = new DataTimers(this);
	public long lastInited = -1L;
	private short ticksExisted = 0;
	public ItemStack itemModel;
	public Block blockModel;
	public boolean needsClientUpdate;
	public int powering;
	public int activePowering;
	public int newPower;
	public int prevPower;
	public boolean isPassible;
	public boolean isLadder;
	public int lightValue;
	public float blockHardness;
	public float blockResistance;
	public int rotationX;
	public int rotationY;
	public int rotationZ;
	public float scaleX;
	public float scaleY;
	public float scaleZ;
	public TileEntity renderTile;
	public boolean renderTileErrored;
	public ITickable renderTileUpdate;
	public TileScripted.TextPlane text1;
	public TileScripted.TextPlane text2;
	public TileScripted.TextPlane text3;
	public TileScripted.TextPlane text4;
	public TileScripted.TextPlane text5;
	public TileScripted.TextPlane text6;

	public TileScripted() {
		this.itemModel = new ItemStack(CustomItems.scripted);
		this.blockModel = null;
		this.needsClientUpdate = false;
		this.powering = 0;
		this.activePowering = 0;
		this.newPower = 0;
		this.prevPower = 0;
		this.isPassible = false;
		this.isLadder = false;
		this.lightValue = 0;
		this.blockHardness = 5.0F;
		this.blockResistance = 10.0F;
		this.rotationX = 0;
		this.rotationY = 0;
		this.rotationZ = 0;
		this.scaleX = 1.0F;
		this.scaleY = 1.0F;
		this.scaleZ = 1.0F;
		this.renderTileErrored = true;
		this.renderTileUpdate = null;
		this.text1 = new TileScripted.TextPlane();
		this.text2 = new TileScripted.TextPlane();
		this.text3 = new TileScripted.TextPlane();
		this.text4 = new TileScripted.TextPlane();
		this.text5 = new TileScripted.TextPlane();
		this.text6 = new TileScripted.TextPlane();
	}

	public IBlock getBlock() {
		if (this.blockDummy == null) {
			this.blockDummy = new BlockScriptedWrapper(this.getWorld(), this.getBlockType(), this.getPos());
		}

		return this.blockDummy;
	}

	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.setNBT(compound);
		this.setDisplayNBT(compound);
		this.timers.readFromNBT(compound);
	}

	public void setNBT(NBTTagCompound compound) {
		this.scripts = NBTTags.GetScript(compound.getTagList("Scripts", 10), this);
		this.scriptLanguage = compound.getString("ScriptLanguage");
		this.enabled = compound.getBoolean("ScriptEnabled");
		this.activePowering = this.powering = compound.getInteger("BlockPowering");
		this.prevPower = compound.getInteger("BlockPrevPower");
		if (compound.hasKey("BlockHardness")) {
			this.blockHardness = compound.getFloat("BlockHardness");
			this.blockResistance = compound.getFloat("BlockResistance");
		}

	}

	public void setDisplayNBT(NBTTagCompound compound) {
		this.itemModel = new ItemStack(compound.getCompoundTag("ScriptBlockModel"));
		if (this.itemModel.isEmpty()) {
			this.itemModel = new ItemStack(CustomItems.scripted);
		}

		if (compound.hasKey("ScriptBlockModelBlock")) {
			this.blockModel = Block.getBlockFromName(compound.getString("ScriptBlockModelBlock"));
		}

		this.renderTileUpdate = null;
		this.renderTile = null;
		this.renderTileErrored = false;
		this.lightValue = compound.getInteger("LightValue");
		this.isLadder = compound.getBoolean("IsLadder");
		this.isPassible = compound.getBoolean("IsPassible");
		this.rotationX = compound.getInteger("RotationX");
		this.rotationY = compound.getInteger("RotationY");
		this.rotationZ = compound.getInteger("RotationZ");
		this.scaleX = compound.getFloat("ScaleX");
		this.scaleY = compound.getFloat("ScaleY");
		this.scaleZ = compound.getFloat("ScaleZ");
		if (this.scaleX <= 0.0F) {
			this.scaleX = 1.0F;
		}

		if (this.scaleY <= 0.0F) {
			this.scaleY = 1.0F;
		}

		if (this.scaleZ <= 0.0F) {
			this.scaleZ = 1.0F;
		}

		if (compound.hasKey("Text3")) {
			this.text1.setNBT(compound.getCompoundTag("Text1"));
			this.text2.setNBT(compound.getCompoundTag("Text2"));
			this.text3.setNBT(compound.getCompoundTag("Text3"));
			this.text4.setNBT(compound.getCompoundTag("Text4"));
			this.text5.setNBT(compound.getCompoundTag("Text5"));
			this.text6.setNBT(compound.getCompoundTag("Text6"));
		}

	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		this.getNBT(compound);
		this.getDisplayNBT(compound);
		this.timers.writeToNBT(compound);
		return super.writeToNBT(compound);
	}

	public NBTTagCompound getNBT(NBTTagCompound compound) {
		compound.setTag("Scripts", NBTTags.NBTScript(this.scripts));
		compound.setString("ScriptLanguage", this.scriptLanguage);
		compound.setBoolean("ScriptEnabled", this.enabled);
		compound.setInteger("BlockPowering", this.powering);
		compound.setInteger("BlockPrevPower", this.prevPower);
		compound.setFloat("BlockHardness", this.blockHardness);
		compound.setFloat("BlockResistance", this.blockResistance);
		return compound;
	}

	public NBTTagCompound getDisplayNBT(NBTTagCompound compound) {
		NBTTagCompound itemcompound = new NBTTagCompound();
		this.itemModel.writeToNBT(itemcompound);
		if (this.blockModel != null) {
			ResourceLocation resourcelocation = (ResourceLocation) Block.REGISTRY.getNameForObject(this.blockModel);
			compound.setString("ScriptBlockModelBlock", resourcelocation == null ? "" : resourcelocation.toString());
		}

		compound.setTag("ScriptBlockModel", itemcompound);
		compound.setInteger("LightValue", this.lightValue);
		compound.setBoolean("IsLadder", this.isLadder);
		compound.setBoolean("IsPassible", this.isPassible);
		compound.setInteger("RotationX", this.rotationX);
		compound.setInteger("RotationY", this.rotationY);
		compound.setInteger("RotationZ", this.rotationZ);
		compound.setFloat("ScaleX", this.scaleX);
		compound.setFloat("ScaleY", this.scaleY);
		compound.setFloat("ScaleZ", this.scaleZ);
		compound.setTag("Text1", this.text1.getNBT());
		compound.setTag("Text2", this.text2.getNBT());
		compound.setTag("Text3", this.text3.getNBT());
		compound.setTag("Text4", this.text4.getNBT());
		compound.setTag("Text5", this.text5.getNBT());
		compound.setTag("Text6", this.text6.getNBT());
		return compound;
	}

	private boolean isEnabled() {
		return this.enabled && ScriptController.HasStart && !this.world.isRemote;
	}

	public void update() {
		if (this.renderTileUpdate != null) {
			try {
				this.renderTileUpdate.update();
			} catch (Exception var2) {
				this.renderTileUpdate = null;
			}
		}

		++this.ticksExisted;
		if (this.prevPower != this.newPower && this.powering <= 0) {
			EventHooks.onScriptBlockRedstonePower(this, this.prevPower, this.newPower);
			this.prevPower = this.newPower;
		}

		this.timers.update();
		if (this.ticksExisted >= 10) {
			EventHooks.onScriptBlockUpdate(this);
			this.ticksExisted = 0;
			if (this.needsClientUpdate) {
				this.markDirty();
				IBlockState state = this.world.getBlockState(this.pos);
				this.world.notifyBlockUpdate(this.pos, state, state, 3);
				this.needsClientUpdate = false;
			}
		}

	}

	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.handleUpdateTag(pkt.getNbtCompound());
	}

	public void handleUpdateTag(NBTTagCompound tag) {
		int light = this.lightValue;
		this.setDisplayNBT(tag);
		if (light != this.lightValue) {
			this.world.checkLight(this.pos);
		}

	}

	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}

	public NBTTagCompound getUpdateTag() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("x", this.pos.getX());
		compound.setInteger("y", this.pos.getY());
		compound.setInteger("z", this.pos.getZ());
		this.getDisplayNBT(compound);
		return compound;
	}

	public void setItemModel(ItemStack item, Block b) {
		if (item == null || item.isEmpty()) {
			item = new ItemStack(CustomItems.scripted);
		}

		if (!NoppesUtilPlayer.compareItems(item, this.itemModel, false, false) || b == this.blockModel) {
			this.itemModel = item;
			this.blockModel = b;
			this.needsClientUpdate = true;
		}
	}

	public void setLightValue(int value) {
		if (value != this.lightValue) {
			this.lightValue = ValueUtil.CorrectInt(value, 0, 15);
			this.needsClientUpdate = true;
		}
	}

	public void setRedstonePower(int strength) {
		if (this.powering != strength) {
			this.prevPower = this.activePowering = ValueUtil.CorrectInt(strength, 0, 15);
			this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
			this.powering = this.activePowering;
		}
	}

	public void setScale(float x, float y, float z) {
		if (this.scaleX != x || this.scaleY != y || this.scaleZ != z) {
			this.scaleX = ValueUtil.correctFloat(x, 0.0F, 10.0F);
			this.scaleY = ValueUtil.correctFloat(y, 0.0F, 10.0F);
			this.scaleZ = ValueUtil.correctFloat(z, 0.0F, 10.0F);
			this.needsClientUpdate = true;
		}
	}

	public void setRotation(int x, int y, int z) {
		if (this.rotationX != x || this.rotationY != y || this.rotationZ != z) {
			this.rotationX = ValueUtil.CorrectInt(x, 0, 359);
			this.rotationY = ValueUtil.CorrectInt(y, 0, 359);
			this.rotationZ = ValueUtil.CorrectInt(z, 0, 359);
			this.needsClientUpdate = true;
		}
	}

	public void runScript(EnumScriptType type, Event event) {
		if (this.isEnabled()) {
			if (ScriptController.Instance.lastLoaded > this.lastInited) {
				this.lastInited = ScriptController.Instance.lastLoaded;
				if (type != EnumScriptType.INIT) {
					EventHooks.onScriptBlockInit(this);
				}
			}

			Iterator var3 = this.scripts.iterator();

			while (var3.hasNext()) {
				ScriptContainer script = (ScriptContainer) var3.next();
				script.run(type, event);
			}

		}
	}

	public boolean isClient() {
		return this.getWorld().isRemote;
	}

	public boolean getEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean bo) {
		this.enabled = bo;
	}

	public String noticeString() {
		BlockPos pos = this.getPos();
		return MoreObjects.toStringHelper(this).add("x", pos.getX()).add("y", pos.getY()).add("z", pos.getZ())
				.toString();
	}

	public String getLanguage() {
		return this.scriptLanguage;
	}

	public void setLanguage(String lang) {
		this.scriptLanguage = lang;
	}

	public List getScripts() {
		return this.scripts;
	}

	public Map getConsoleText() {
		Map map = new TreeMap();
		int tab = 0;
		Iterator var3 = this.getScripts().iterator();

		while (var3.hasNext()) {
			ScriptContainer script = (ScriptContainer) var3.next();
			++tab;
			Iterator var5 = script.console.entrySet().iterator();

			while (var5.hasNext()) {
				Entry entry = (Entry) var5.next();
				map.put(entry.getKey(), " tab " + tab + ":\n" + (String) entry.getValue());
			}
		}

		return map;
	}

	public void clearConsole() {
		Iterator var1 = this.getScripts().iterator();

		while (var1.hasNext()) {
			ScriptContainer script = (ScriptContainer) var1.next();
			script.console.clear();
		}

	}

	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return Block.FULL_BLOCK_AABB.offset(this.getPos());
	}

	public class TextPlane implements ITextPlane {
		public boolean textHasChanged = true;
		public TextBlock textBlock;
		public String text = "";
		public int rotationX = 0;
		public int rotationY = 0;
		public int rotationZ = 0;
		public float offsetX = 0.0F;
		public float offsetY = 0.0F;
		public float offsetZ = 0.5F;
		public float scale = 1.0F;

		public String getText() {
			return this.text;
		}

		public void setText(String text) {
			if (!this.text.equals(text)) {
				this.text = text;
				this.textHasChanged = true;
				TileScripted.this.needsClientUpdate = true;
			}
		}

		public int getRotationX() {
			return this.rotationX;
		}

		public int getRotationY() {
			return this.rotationY;
		}

		public int getRotationZ() {
			return this.rotationZ;
		}

		public void setRotationX(int x) {
			x = ValueUtil.CorrectInt(x % 360, 0, 359);
			if (this.rotationX != x) {
				this.rotationX = x;
				TileScripted.this.needsClientUpdate = true;
			}
		}

		public void setRotationY(int y) {
			y = ValueUtil.CorrectInt(y % 360, 0, 359);
			if (this.rotationY != y) {
				this.rotationY = y;
				TileScripted.this.needsClientUpdate = true;
			}
		}

		public void setRotationZ(int z) {
			z = ValueUtil.CorrectInt(z % 360, 0, 359);
			if (this.rotationZ != z) {
				this.rotationZ = z;
				TileScripted.this.needsClientUpdate = true;
			}
		}

		public float getOffsetX() {
			return this.offsetX;
		}

		public float getOffsetY() {
			return this.offsetY;
		}

		public float getOffsetZ() {
			return this.offsetZ;
		}

		public void setOffsetX(float x) {
			x = ValueUtil.correctFloat(x, -1.0F, 1.0F);
			if (this.offsetX != x) {
				this.offsetX = x;
				TileScripted.this.needsClientUpdate = true;
			}
		}

		public void setOffsetY(float y) {
			y = ValueUtil.correctFloat(y, -1.0F, 1.0F);
			if (this.offsetY != y) {
				this.offsetY = y;
				TileScripted.this.needsClientUpdate = true;
			}
		}

		public void setOffsetZ(float z) {
			z = ValueUtil.correctFloat(z, -1.0F, 1.0F);
			if (this.offsetZ != z) {
				System.out.println(this.rotationZ);
				this.offsetZ = z;
				TileScripted.this.needsClientUpdate = true;
			}
		}

		public float getScale() {
			return this.scale;
		}

		public void setScale(float scale) {
			if (this.scale != scale) {
				this.scale = scale;
				TileScripted.this.needsClientUpdate = true;
			}
		}

		public NBTTagCompound getNBT() {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("Text", this.text);
			compound.setInteger("RotationX", this.rotationX);
			compound.setInteger("RotationY", this.rotationY);
			compound.setInteger("RotationZ", this.rotationZ);
			compound.setFloat("OffsetX", this.offsetX);
			compound.setFloat("OffsetY", this.offsetY);
			compound.setFloat("OffsetZ", this.offsetZ);
			compound.setFloat("Scale", this.scale);
			return compound;
		}

		public void setNBT(NBTTagCompound compound) {
			this.setText(compound.getString("Text"));
			this.rotationX = compound.getInteger("RotationX");
			this.rotationY = compound.getInteger("RotationY");
			this.rotationZ = compound.getInteger("RotationZ");
			this.offsetX = compound.getFloat("OffsetX");
			this.offsetY = compound.getFloat("OffsetY");
			this.offsetZ = compound.getFloat("OffsetZ");
			this.scale = compound.getFloat("Scale");
		}
	}
}
