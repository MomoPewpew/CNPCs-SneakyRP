package noppes.npcs.client.renderer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import net.minecraft.client.renderer.ImageBufferDownload;

public class ImageBufferDownloadAlt extends ImageBufferDownload {
	private int[] imageData;
	private int imageWidth;
	private int imageHeight;

	public BufferedImage parseUserSkin(BufferedImage bufferedimage) {
		this.imageWidth = bufferedimage.getWidth((ImageObserver) null);
		this.imageHeight = bufferedimage.getHeight((ImageObserver) null);
		BufferedImage bufferedimage1 = new BufferedImage(this.imageWidth, this.imageHeight, 2);
		Graphics graphics = bufferedimage1.getGraphics();
		graphics.drawImage(bufferedimage, 0, 0, (ImageObserver) null);
		graphics.dispose();
		this.imageData = ((DataBufferInt) bufferedimage1.getRaster().getDataBuffer()).getData();
		return bufferedimage1;
	}

	private void setAreaTransparent(int par1, int par2, int par3, int par4) {
		if (!this.hasTransparency(par1, par2, par3, par4)) {
			for (int i1 = par1; i1 < par3; ++i1) {
				for (int j1 = par2; j1 < par4; ++j1) {
					int[] var10000 = this.imageData;
					int var10001 = i1 + j1 * this.imageWidth;
					var10000[var10001] &= 16777215;
				}
			}
		}

	}

	private boolean hasTransparency(int par1, int par2, int par3, int par4) {
		for (int i1 = par1; i1 < par3; ++i1) {
			for (int j1 = par2; j1 < par4; ++j1) {
				int k1 = this.imageData[i1 + j1 * this.imageWidth];
				if ((k1 >> 24 & 255) < 128) {
					return true;
				}
			}
		}

		return false;
	}
}
