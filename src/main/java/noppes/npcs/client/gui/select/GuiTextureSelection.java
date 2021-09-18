package noppes.npcs.client.gui.select;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.FallbackResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.LegacyV2Adapter;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.ResourcePackRepository.Entry;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import noppes.npcs.CustomNpcs;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiTextureSelection extends SubGuiInterface implements ICustomScrollListener {
	private String up = "..<" + I18n.translateToLocal("gui.up") + ">..";
	private GuiCustomScroll scrollCategories;
	private GuiCustomScroll scrollQuests;
	private String location = "";
	private String selectedDomain;
	public ResourceLocation selectedResource;
	private HashMap domains = new HashMap();
	private HashMap textures = new HashMap();

	public GuiTextureSelection(EntityNPCInterface npc, String texture) {
		this.npc = npc;
		this.drawDefaultBackground = false;
		this.title = "";
		this.setBackground("menubg.png");
		this.xSize = 366;
		this.ySize = 226;
		SimpleReloadableResourceManager simplemanager = (SimpleReloadableResourceManager) Minecraft.getMinecraft()
				.getResourceManager();
		Map map = (Map) ObfuscationReflectionHelper.getPrivateValue(SimpleReloadableResourceManager.class,
				simplemanager, 2);
		HashSet set = new HashSet();
		Iterator var6 = map.keySet().iterator();

		String file;
		while (var6.hasNext()) {
			file = (String) var6.next();
			FallbackResourceManager manager = (FallbackResourceManager) map.get(file);
			List list = (List) ObfuscationReflectionHelper.getPrivateValue(FallbackResourceManager.class, manager, 1);
			Iterator var10 = list.iterator();

			while (var10.hasNext()) {
				IResourcePack pack = (IResourcePack) var10.next();
				if (pack instanceof LegacyV2Adapter) {
					pack = (IResourcePack) ObfuscationReflectionHelper.getPrivateValue(LegacyV2Adapter.class,
							(LegacyV2Adapter) pack, 0);
				}

				if (pack instanceof AbstractResourcePack) {
					AbstractResourcePack p = (AbstractResourcePack) pack;
					File file1 = (File)ObfuscationReflectionHelper.getPrivateValue(AbstractResourcePack.class, p, "resourcePackFile");
					// File file1 = p.resourcePackFile;
					if (file1 != null) {
						set.add(file1.getAbsolutePath());
					}
				}
			}
		}

		var6 = set.iterator();

		while (var6.hasNext()) {
			file = (String) var6.next();
			File f = new File(file);
			if (f.isDirectory()) {
				this.checkFolder(new File(f, "assets"), f.getAbsolutePath().length());
			} else {
				this.progressFile(f);
			}
		}

		var6 = Loader.instance().getModList().iterator();

		while (var6.hasNext()) {
			ModContainer mod = (ModContainer) var6.next();
			if (mod.getSource().exists()) {
				this.progressFile(mod.getSource());
			}
		}

		ResourcePackRepository repos = Minecraft.getMinecraft().getResourcePackRepository();
		repos.updateRepositoryEntriesAll();
		List list = repos.getRepositoryEntries();
		File f;
		if (repos.getServerResourcePack() != null) {
			AbstractResourcePack p = (AbstractResourcePack) repos.getServerResourcePack();
			f = (File)ObfuscationReflectionHelper.getPrivateValue(AbstractResourcePack.class, p, "resourcePackFile");
			// f = p.resourcePackFile;
			if (f != null) {
				this.progressFile(f);
			}
		}

		Iterator var19 = list.iterator();

		while (var19.hasNext()) {
			Entry entry = (Entry) var19.next();
			File file1 = new File(repos.getDirResourcepacks(), entry.getResourcePackName());
			if (file1.exists()) {
				this.progressFile(file1);
			}
		}

		this.checkFolder(new File(CustomNpcs.Dir, "assets"), CustomNpcs.Dir.getAbsolutePath().length());
		URL url = DefaultResourcePack.class.getResource("/");
		if (url != null) {
			f = this.decodeFile(url.getFile());
			if (f.isDirectory()) {
				this.checkFolder(new File(f, "assets"), url.getFile().length());
			} else {
				this.progressFile(f);
			}
		}

		url = CraftingManager.class.getResource("/assets/.mcassetsroot");
		if (url != null) {
			f = this.decodeFile(url.getFile());
			if (f.isDirectory()) {
				this.checkFolder(new File(f, "assets"), url.getFile().length());
			} else {
				this.progressFile(f);
			}
		}

		if (texture != null && !texture.isEmpty()) {
			this.selectedResource = new ResourceLocation(texture);
			this.selectedDomain = this.selectedResource.getResourceDomain();
			if (!this.domains.containsKey(this.selectedDomain)) {
				this.selectedDomain = null;
			}

			int i = this.selectedResource.getResourcePath().lastIndexOf(47);
			this.location = this.selectedResource.getResourcePath().substring(0, i + 1);
			if (this.location.startsWith("textures/")) {
				this.location = this.location.substring(9);
			}
		}

	}

	private File decodeFile(String url) {
		if (url.startsWith("file:")) {
			url = url.substring(5);
		}

		url = url.replace('/', File.separatorChar);
		int i = url.indexOf(33);
		if (i > 0) {
			url = url.substring(0, i);
		}

		try {
			url = URLDecoder.decode(url, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException var4) {
		}

		return new File(url);
	}

	public void initGui() {
		super.initGui();
		if (this.selectedDomain != null) {
			this.title = this.selectedDomain + ":" + this.location;
		} else {
			this.title = "";
		}

		this.addButton(new GuiNpcButton(2, this.guiLeft + 264, this.guiTop + 170, 90, 20, "gui.done"));
		this.addButton(new GuiNpcButton(1, this.guiLeft + 264, this.guiTop + 190, 90, 20, "gui.cancel"));
		if (this.scrollCategories == null) {
			this.scrollCategories = new GuiCustomScroll(this, 0);
			this.scrollCategories.setSize(120, 200);
		}

		if (this.selectedDomain == null) {
			this.scrollCategories.setList(Lists.newArrayList(this.domains.keySet()));
			if (this.selectedDomain != null) {
				this.scrollCategories.setSelected(this.selectedDomain);
			}
		} else {
			List list = new ArrayList();
			list.add(this.up);
			List data = (List) this.domains.get(this.selectedDomain);
			Iterator var3 = data.iterator();

			label75: while (true) {
				GuiTextureSelection.TextureData td;
				do {
					if (!var3.hasNext()) {
						this.scrollCategories.setList(list);
						break label75;
					}

					td = (GuiTextureSelection.TextureData) var3.next();
				} while (!this.location.isEmpty()
						&& (!td.path.startsWith(this.location) || td.path.equals(this.location)));

				String path = td.path.substring(this.location.length());
				int i = path.indexOf(47);
				if (i >= 0) {
					path = path.substring(0, i);
					if (!path.isEmpty() && !list.contains(path)) {
						list.add(path);
					}
				}
			}
		}

		this.scrollCategories.guiLeft = this.guiLeft + 4;
		this.scrollCategories.guiTop = this.guiTop + 14;
		this.addScroll(this.scrollCategories);
		if (this.scrollQuests == null) {
			this.scrollQuests = new GuiCustomScroll(this, 1);
			this.scrollQuests.setSize(130, 200);
		}

		if (this.selectedDomain != null) {
			this.textures.clear();
			List data = (List) this.domains.get(this.selectedDomain);
			List list = new ArrayList();
			String loc = this.location;
			if (this.scrollCategories.hasSelected() && !this.scrollCategories.getSelected().equals(this.up)) {
				loc = loc + this.scrollCategories.getSelected() + '/';
			}

			Iterator var10 = data.iterator();

			while (var10.hasNext()) {
				GuiTextureSelection.TextureData td = (GuiTextureSelection.TextureData) var10.next();
				if (td.path.equals(loc) && !list.contains(td.name)) {
					list.add(td.name);
					this.textures.put(td.name, td);
				}
			}

			this.scrollQuests.setList(list);
		}

		if (this.selectedResource != null) {
			this.scrollQuests.setSelected(this.selectedResource.getResourcePath());
		}

		this.scrollQuests.guiLeft = this.guiLeft + 125;
		this.scrollQuests.guiTop = this.guiTop + 14;
		this.addScroll(this.scrollQuests);
	}

	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);
		if (guibutton.id == 2) {
			this.npc.display.setSkinTexture(this.selectedResource.toString());
		}

		this.npc.textureLocation = null;
		this.close();
		this.parent.initGui();
	}

	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
		this.npc.textureLocation = this.selectedResource;
		this.drawNpc(this.npc, this.guiLeft + 276, this.guiTop + 140, 2.0F, 0);
	}

	public void scrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
		if (scroll == this.scrollQuests) {
			if (scroll.id == 1) {
				GuiTextureSelection.TextureData data = (GuiTextureSelection.TextureData) this.textures
						.get(scroll.getSelected());
				this.selectedResource = new ResourceLocation(this.selectedDomain, data.absoluteName);
			}
		} else {
			this.initGui();
		}

	}

	public void scrollDoubleClicked(String selection, GuiCustomScroll scroll) {
		if (scroll == this.scrollCategories) {
			if (this.selectedDomain == null) {
				this.selectedDomain = selection;
			} else if (selection.equals(this.up)) {
				int i = this.location.lastIndexOf(47, this.location.length() - 2);
				if (i < 0) {
					if (this.location.isEmpty()) {
						this.selectedDomain = null;
					}

					this.location = "";
				} else {
					this.location = this.location.substring(0, i + 1);
				}
			} else {
				this.location = this.location + selection + '/';
			}

			this.scrollCategories.selected = -1;
			this.scrollQuests.selected = -1;
			this.initGui();
		} else {
			this.npc.display.setSkinTexture(this.selectedResource.toString());
			this.close();
			this.parent.initGui();
		}

	}

	private void progressFile(File file) {
		try {
			if (!file.isDirectory() && (file.getName().endsWith(".jar") || file.getName().endsWith(".zip"))) {
				ZipFile zip = new ZipFile(file);
				Enumeration entries = zip.entries();

				while (entries.hasMoreElements()) {
					ZipEntry zipentry = (ZipEntry) entries.nextElement();
					String entryName = zipentry.getName();
					this.addFile(entryName);
				}

				zip.close();
			} else if (file.isDirectory()) {
				int length = file.getAbsolutePath().length();
				this.checkFolder(file, length);
			}
		} catch (Exception var6) {
			var6.printStackTrace();
		}

	}

	private void checkFolder(File file, int length) {
		File[] files = file.listFiles();
		if (files != null) {
			File[] var4 = files;
			int var5 = files.length;

			for (int var6 = 0; var6 < var5; ++var6) {
				File f = var4[var6];
				String name = f.getAbsolutePath().substring(length);
				name = name.replace("\\", "/");
				if (!name.startsWith("/")) {
					name = "/" + name;
				}

				if (f.isDirectory()) {
					this.addFile(name + "/");
					this.checkFolder(f, length);
				} else {
					this.addFile(name);
				}
			}

		}
	}

	private void addFile(String name) {
		name = name.toLowerCase();
		if (name.startsWith("/")) {
			name = name.substring(1);
		}

		if (name.startsWith("assets/") && name.toLowerCase().endsWith(".png")) {
			name = name.substring(7);
			int i = name.indexOf(47);
			String domain = name.substring(0, i);
			name = name.substring(i + 10);
			List list = (List) this.domains.get(domain);
			if (list == null) {
				this.domains.put(domain, list = new ArrayList());
			}

			boolean contains = false;
			Iterator var6 = ((List) list).iterator();

			while (var6.hasNext()) {
				GuiTextureSelection.TextureData data = (GuiTextureSelection.TextureData) var6.next();
				if (data.absoluteName.equals(name)) {
					contains = true;
					break;
				}
			}

			if (!contains) {
				((List) list).add(new GuiTextureSelection.TextureData(domain, name));
			}

		}
	}

	class TextureData {
		String domain;
		String absoluteName;
		String name;
		String path;

		public TextureData(String domain, String absoluteName) {
			this.domain = domain;
			int i = absoluteName.lastIndexOf(47);
			this.name = absoluteName.substring(i + 1);
			this.path = absoluteName.substring(0, i + 1);
			this.absoluteName = "textures/" + absoluteName;
		}
	}
}
