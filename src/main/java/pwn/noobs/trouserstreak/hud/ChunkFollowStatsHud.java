package pwn.noobs.trouserstreak.hud;

import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import pwn.noobs.trouserstreak.modules.NewerNewChunks;

import java.util.ArrayList;
import java.util.List;

public class ChunkFollowStatsHud extends HudElement {
    public static final HudElementInfo<ChunkFollowStatsHud> INFO = new HudElementInfo<>(
        Hud.GROUP,
        "chunk-follow-stats",
        "Shows NewerNewChunks auto-follow stats (heading, apex, retraced chunks, target, config).",
        ChunkFollowStatsHud::new
    );

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgStyle = settings.createGroup("Style");

    private final Setting<Boolean> background = sgStyle.add(new BoolSetting.Builder()
        .name("background")
        .description("Draw a translucent background.")
        .defaultValue(false)
        .build()
    );

    private final Setting<SettingColor> backgroundColor = sgStyle.add(new ColorSetting.Builder()
        .name("background-color")
        .description("Background color.")
        .defaultValue(new SettingColor(25, 25, 25, 50))
        .visible(background::get)
        .build()
    );

    private final Setting<Double> scale = sgStyle.add(new DoubleSetting.Builder()
        .name("scale")
        .description("Text scale.")
        .defaultValue(1.0)
        .min(0.7)
        .sliderRange(0.7, 2.0)
        .build()
    );

    public ChunkFollowStatsHud() {
        super(INFO);
        setSize(140, 72);
    }

    @Override
    public void render(HudRenderer renderer) {
        NewerNewChunks mod = Modules.get().get(NewerNewChunks.class);
        if (mod == null) {
            drawLines(renderer, List.of("NewerNewChunks not loaded"));
            return;
        }

        ChunkPos target = mod.hudCurrentTarget();
        Direction heading = mod.hudHeading();
        ChunkPos apex = mod.hudBacktrackApex();

        String followType = String.valueOf(mod.hudFollowType());
        String targetStr = target != null ? (target.x + "," + target.z) : "-";
        String headStr = heading != null ? heading.asString() : "-";
        String apexStr = apex != null ? (apex.x + "," + apex.z) : "-";

        int retraced = mod.hudRetracedChunks();
        int gap = mod.hudGapAllowance();
        int limit = mod.hudBacktrackLimit();
        int pool = mod.hudPoolSize();

        List<String> lines = new ArrayList<>();
        lines.add("Follow: " + followType + " (pool=" + pool + ")");
        lines.add("Target: " + targetStr);
        lines.add("Heading: " + headStr);
        lines.add("Apex: " + apexStr);
        lines.add("Retraced: " + retraced + "/" + limit + " chunks");
        lines.add("Gap: " + gap + " chunks");

        drawLines(renderer, lines);
    }

    private void drawLines(HudRenderer renderer, List<String> lines) {
        double sx = x;
        double sy = y;
        double w = 0;
        double h = 0;

        // Measure
        for (String s : lines) {
            double tw = renderer.textWidth(s, true, scale.get());
            w = Math.max(w, tw);
            h += renderer.textHeight(true, scale.get());
        }
        setSize(w + 4, h + 4);

        if (background.get()) renderer.quad(sx, sy, getWidth(), getHeight(), backgroundColor.get());

        double cy = sy + 2;
        for (String s : lines) {
            renderer.text(s, sx + 2, cy, Color.WHITE, true, scale.get());
            cy += renderer.textHeight(true, scale.get());
        }
    }
}

