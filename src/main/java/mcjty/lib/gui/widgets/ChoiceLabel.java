package mcjty.lib.gui.widgets;

import mcjty.lib.gui.RenderHelper;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.events.ChoiceEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.util.*;

public class ChoiceLabel extends Label<ChoiceLabel> {
    private List<String> choiceList = new ArrayList<String>();
    private Map<String,List<String>> tooltipMap = new HashMap<String, List<String>>();
    private String currentChoice = null;
    private List<ChoiceEvent> choiceEvents = null;

    public ChoiceLabel(Minecraft mc, Gui gui) {
        super(mc, gui);
        setText("");
    }

    public ChoiceLabel addChoices(String ... choices) {
        for (String choice : choices) {
            choiceList.add(choice);
            if (currentChoice == null) {
                currentChoice = choice;
                setText(currentChoice);
                fireChoiceEvents(currentChoice);
            }
        }
        return this;
    }

    public ChoiceLabel setChoiceTooltip(String choice, String... tooltips) {
        tooltipMap.put(choice, Arrays.asList(tooltips));
        return this;
    }

    public ChoiceLabel setChoice(String choice) {
        currentChoice = choice;
        setText(currentChoice);
        return this;
    }

    public String getCurrentChoice() {
        return currentChoice;
    }

    @Override
    public List<String> getTooltips() {
        List<String> tooltips = tooltipMap.get(currentChoice);
        if (tooltips == null) {
            return super.getTooltips();
        } else {
            return tooltips;
        }
    }

    @Override
    public void draw(Window window, int x, int y) {
        if (!visible) {
            return;
        }
        int xx = x + bounds.x;
        int yy = y + bounds.y;

        if (isEnabled()) {
            RenderHelper.drawThinButtonBox(xx, yy, xx + bounds.width - 1, yy + bounds.height - 1, 0xffeeeeee, 0xffc6c6c6, 0xff333333);
            RenderHelper.drawUpTriangle(xx + bounds.width - 7, yy + bounds.height / 2 - 4, 0xff000000);
            RenderHelper.drawDownTriangle(xx + bounds.width - 7, yy + bounds.height / 2 + 2, 0xff000000);
        } else {
            RenderHelper.drawThinButtonBox(xx, yy, xx + bounds.width - 1, yy + bounds.height - 1, 0xff888888, 0xffc6c6c6, 0xff555555);
            RenderHelper.drawUpTriangle(xx + bounds.width - 7, yy + bounds.height / 2 - 4, 0xff888888);
            RenderHelper.drawDownTriangle(xx + bounds.width - 7, yy + bounds.height / 2 + 2, 0xff888888);
        }

        super.drawOffset(window, x, y, 0, 1);
    }

    @Override
    public Widget mouseClick(Window window, int x, int y, int button) {
        if (isEnabledAndVisible()) {
            if (choiceList.isEmpty()) {
                return null;
            }
            int index = choiceList.indexOf(currentChoice);
            index++;
            if (index >= choiceList.size()) {
                index = 0;
            }
            currentChoice = choiceList.get(index);
            setText(currentChoice);
            fireChoiceEvents(currentChoice);
        }
        return null;
    }

    public ChoiceLabel addChoiceEvent(ChoiceEvent event) {
        if (choiceEvents == null) {
            choiceEvents = new ArrayList<ChoiceEvent>();
        }
        choiceEvents.add(event);
        return this;
    }

    public void removeChoiceEvent(ChoiceEvent event) {
        if (choiceEvents != null) {
            choiceEvents.remove(event);
        }
    }

    private void fireChoiceEvents(String choice) {
        if (choiceEvents != null) {
            for (ChoiceEvent event : choiceEvents) {
                event.choiceChanged(this, choice);
            }
        }
    }
}