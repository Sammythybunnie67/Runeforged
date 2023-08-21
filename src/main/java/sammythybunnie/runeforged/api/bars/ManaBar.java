package sammythybunnie.runeforged.api.bars;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.TextureFX;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.Global;
import net.minecraft.core.block.Block;
import org.lwjgl.opengl.GL11;
import sammythybunnie.runeforged.api.uuidAPI.UUIDHelper;
import toufoumaster.btwaila.util.TextureOptions;

import java.util.UUID;

public class ManaBar {

        private Minecraft theGame;
        private ItemEntityRenderer itemRender;

        public void drawTextureRectRepeat(int x, int y, int w, int h, int texX, int texY, int tileWidth, int color) {
                float r = (float)(color >> 16 & 0xFF) / 255.0f;
                float g = (float)(color >> 8 & 0xFF) / 255.0f;
                float b = (float)(color & 0xFF) / 255.0f;
                GL11.glColor4f(r, g, b, 1f);
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                GL11.glScissor(0, this.theGame.resolution.height - h * this.theGame.resolution.scale, w * this.theGame.resolution.scale, this.theGame.resolution.height);

                for (int i = x; i < w; i += tileWidth) {
                        for (int j = y; j < h; j += tileWidth) {
                                this.itemRender.renderTexturedQuad(i, j, texX, texY, tileWidth, tileWidth);
                        }
                }
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }

        public void drawManaBarTexture(int value, int max, int boxWidth, TextureOptions bgOptions, TextureOptions fgOptions, int offX) {
                RenderEngine renderEngine = this.theGame.renderEngine;
                renderEngine.bindTexture(renderEngine.getTexture("/terrain.png"));
                int tileWidth = TextureFX.tileWidthTerrain;
                int bgTexId = Block.getBlock(bgOptions.blockId).getBlockTextureFromSideAndMetadata(bgOptions.side, bgOptions.metadata);
                int bgTexX = bgTexId % Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth;
                int bgTexY = bgTexId / Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth;

                int x = 0;
                int y = 0;
                int width = 0;
                int height = 0;
                drawTextureRectRepeat(x, y, x + width, y + height, bgTexX, bgTexY, tileWidth, bgOptions.color);

                // Render mana bar using player's UUID
                UUID playerUUID = UUIDHelper.getPlayerUUIDFor(this.theGame.thePlayer); // Assuming UUIDHelper class has this method
                int manaValue = UUIDHelper.getMana(playerUUID, "currentWorld"); // Get the mana value using the UUID

                // Render mana bar using manaValue

        }
}
