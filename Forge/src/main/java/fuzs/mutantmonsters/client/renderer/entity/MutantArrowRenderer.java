package fuzs.mutantmonsters.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import fuzs.mutantmonsters.MutantMonsters;
import fuzs.mutantmonsters.client.init.ClientModRegistry;
import fuzs.mutantmonsters.client.renderer.entity.model.MutantArrowModel;
import fuzs.mutantmonsters.entity.projectile.MutantArrowEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class MutantArrowRenderer extends EntityRenderer<MutantArrowEntity> {
    public static final ResourceLocation TEXTURE = MutantMonsters.getEntityTexture("mutant_arrow");

    private final MutantArrowModel model;

    public MutantArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new MutantArrowModel(context.bakeLayer(ClientModRegistry.MUTANT_ARROW));
    }

    @Override
    public boolean shouldRender(MutantArrowEntity livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void render(MutantArrowEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);

        for(int i = 0; i < entityIn.getClones(); ++i) {
            matrixStackIn.pushPose();
            float scale = entityIn.getSpeed() - (float)i * 0.08F;
            double x = (entityIn.getTargetX() - entityIn.getX()) * (double)((float)entityIn.tickCount + partialTicks) * (double)scale;
            double y = (entityIn.getTargetY() - entityIn.getY()) * (double)((float)entityIn.tickCount + partialTicks) * (double)scale;
            double z = (entityIn.getTargetZ() - entityIn.getZ()) * (double)((float)entityIn.tickCount + partialTicks) * (double)scale;
            matrixStackIn.translate(x, y, z);
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(entityIn.getYRot()));
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(entityIn.getXRot()));
            matrixStackIn.scale(1.2F, 1.2F, 1.2F);
            VertexConsumer vertexBuilder = bufferIn.getBuffer(this.model.renderType(TEXTURE));
            this.model.renderToBuffer(matrixStackIn, vertexBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F - (float)i * 0.08F);
            matrixStackIn.popPose();
        }

    }

    @Override
    public ResourceLocation getTextureLocation(MutantArrowEntity entity) {
        return TEXTURE;
    }
}
