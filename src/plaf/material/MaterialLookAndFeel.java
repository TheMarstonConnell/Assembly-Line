package plaf.material;

import java.awt.Color;
import java.security.AccessController;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.text.DefaultEditorKit;

import com.mic.assembly.AssemblyMachine;
import com.mic.assembly.LineHighlightPane;

import plaf.material.objects.MaterialButtonUI;
import plaf.material.objects.MaterialCheckBoxMenuItemUI;
import plaf.material.objects.MaterialCheckBoxUI;
import plaf.material.objects.MaterialComboBoxUI;
import plaf.material.objects.MaterialFileChooserUI;
import plaf.material.objects.MaterialFormattedTextFieldUI;
import plaf.material.objects.MaterialLabelUI;
import plaf.material.objects.MaterialMenuBarUI;
import plaf.material.objects.MaterialMenuItemUI;
import plaf.material.objects.MaterialMenuUI;
import plaf.material.objects.MaterialOptionPaneUI;
import plaf.material.objects.MaterialPanelUI;
import plaf.material.objects.MaterialPasswordFieldUI;
import plaf.material.objects.MaterialPopupMenuUI;
import plaf.material.objects.MaterialProgressBarUI;
import plaf.material.objects.MaterialRadioButtonMenuItemUI;
import plaf.material.objects.MaterialRadioButtonUI;
import plaf.material.objects.MaterialScrollBarUI;
import plaf.material.objects.MaterialSeparatorUI;
import plaf.material.objects.MaterialSliderUI;
import plaf.material.objects.MaterialSpinnerUI;
import plaf.material.objects.MaterialTabbedPaneUI;
import plaf.material.objects.MaterialTableHeaderUI;
import plaf.material.objects.MaterialTableUI;
import plaf.material.objects.MaterialTextFieldUI;
import plaf.material.objects.MaterialTextPaneUI;
import plaf.material.objects.MaterialToggleButtonUI;
import plaf.material.objects.MaterialToolBarUI;
import plaf.material.objects.MaterialToolTipUI;
import plaf.material.objects.MaterialTreeUI;
import plaf.material.utils.MaterialBorders;
import plaf.material.utils.MaterialColors;
import plaf.material.utils.MaterialFonts;
import plaf.material.utils.MaterialImages;

/**
 * https://github.com/atarw/material-ui-swing
 * Taken almost completely from this repo. Used to just make the application prettier.
 * @author Marston Connell
 *
 */
public class MaterialLookAndFeel extends BasicLookAndFeel {

	public MaterialColors colors;

	private static final String buttonUI = MaterialButtonUI.class.getCanonicalName();
	private static final String textfieldUI = MaterialTextFieldUI.class.getCanonicalName();
	private static final String passwordFieldUI = MaterialPasswordFieldUI.class.getCanonicalName();
	private static final String tableUI = MaterialTableUI.class.getCanonicalName();
	private static final String tableHeaderUI = MaterialTableHeaderUI.class.getCanonicalName();
	private static final String treeUI = MaterialTreeUI.class.getCanonicalName();
	private static final String spinnerUI = MaterialSpinnerUI.class.getCanonicalName();
	private static final String panelUI = MaterialPanelUI.class.getCanonicalName();
	private static final String labelUI = MaterialLabelUI.class.getCanonicalName();
	private static final String menuItemUI = MaterialMenuItemUI.class.getCanonicalName();
	private static final String menuBarUI = MaterialMenuBarUI.class.getCanonicalName();
	private static final String menuUI = MaterialMenuUI.class.getCanonicalName();
	private static final String checkBoxUI = MaterialCheckBoxUI.class.getCanonicalName();
	private static final String radioButtonUI = MaterialRadioButtonUI.class.getCanonicalName();
	private static final String tabbedPaneUI = MaterialTabbedPaneUI.class.getCanonicalName();
	private static final String toggleButtonUI = MaterialToggleButtonUI.class.getCanonicalName();
	private static final String scrollBarUI = MaterialScrollBarUI.class.getCanonicalName();
	private static final String comboBoxUI = MaterialComboBoxUI.class.getCanonicalName();
	private static final String popupMenuUI = MaterialPopupMenuUI.class.getCanonicalName();
	private static final String toolbarUI = MaterialToolBarUI.class.getCanonicalName();
	private static final String sliderUI = MaterialSliderUI.class.getCanonicalName();
	private static final String progressBarUI = MaterialProgressBarUI.class.getCanonicalName();
	private static final String radioButtonMenuItemUI = MaterialRadioButtonMenuItemUI.class.getCanonicalName();
	private static final String checkBoxMenuItemUI = MaterialCheckBoxMenuItemUI.class.getCanonicalName();
	private static final String textPaneUI = LineHighlightPane.class.getCanonicalName();
	private static final String editorPane = MaterialTextPaneUI.class.getCanonicalName();
	private static final String separatorUI = MaterialSeparatorUI.class.getCanonicalName();
	private static final String fileChooserUI = MaterialFileChooserUI.class.getCanonicalName();
	private static final String toolTipUI = MaterialToolTipUI.class.getCanonicalName();
	private static final String optionPaneUI = MaterialOptionPaneUI.class.getCanonicalName();
	private static final String formattedTextFieldUI = MaterialFormattedTextFieldUI.class.getCanonicalName();

	@Override
	public String getDescription() {
		return "The look and feel used by Marston Connell to make better looking Java apps";
	}

	@Override
	public String getID() {
		return "Material";
	}

	@Override
	public String getName() {
		return "Material";
	}

	@Override
	public boolean isNativeLookAndFeel() {
		return false;
	}

	@Override
	public boolean isSupportedLookAndFeel() {
		return true;
	}

	@Override
	protected void initClassDefaults(UIDefaults table) {
		super.initClassDefaults(table);
		table.put("ButtonUI", buttonUI);
		table.put("TextFieldUI", textfieldUI);
		table.put("PasswordFieldUI", passwordFieldUI);
		table.put("TableUI", tableUI);
		table.put("TableHeaderUI", tableHeaderUI);
		table.put("TreeUI", treeUI);
		table.put("SpinnerUI", spinnerUI);
		table.put("PanelUI", panelUI);
		table.put("LabelUI", labelUI);
		table.put("MenuItemUI", menuItemUI);
		table.put("MenuBarUI", menuBarUI);
		table.put("MenuUI", menuUI);
		table.put("CheckBoxUI", checkBoxUI);
		table.put("RadioButtonUI", radioButtonUI);
		table.put("TabbedPaneUI", tabbedPaneUI);
		table.put("ToggleButtonUI", toggleButtonUI);
		table.put("ScrollBarUI", scrollBarUI);
		table.put("ComboBoxUI", comboBoxUI);
		table.put("PopupMenuUI", popupMenuUI);
		table.put("ToolBarUI", toolbarUI);
		table.put("SliderUI", sliderUI);
		table.put("ProgressBarUI", progressBarUI);
		table.put("RadioButtonMenuItemUI", radioButtonMenuItemUI);
		table.put("CheckBoxMenuItemUI", checkBoxMenuItemUI);
		table.put("TextPaneUI", textPaneUI);
		table.put("EditorPaneUI", editorPane);
		table.put("SeparatorUI", separatorUI);
		table.put("FileChooserUI", fileChooserUI);
		table.put("ToolTipUI", toolTipUI);
		table.put("OptionPaneUI", optionPaneUI);
		table.put("FormattedTextFieldUI", formattedTextFieldUI);
	}

	Object multilineInputMap = new UIDefaults.LazyInputMap(new Object[] { "ctrl C", DefaultEditorKit.copyAction,
			"ctrl V", DefaultEditorKit.pasteAction, "ctrl X", DefaultEditorKit.cutAction, "COPY",
			DefaultEditorKit.copyAction, "PASTE", DefaultEditorKit.pasteAction, "CUT", DefaultEditorKit.cutAction,
			"control INSERT", DefaultEditorKit.copyAction, "shift INSERT", DefaultEditorKit.pasteAction, "shift DELETE",
			DefaultEditorKit.cutAction, "shift LEFT", DefaultEditorKit.selectionBackwardAction, "shift KP_LEFT",
			DefaultEditorKit.selectionBackwardAction, "shift RIGHT", DefaultEditorKit.selectionForwardAction,
			"shift KP_RIGHT", DefaultEditorKit.selectionForwardAction, "ctrl LEFT", DefaultEditorKit.previousWordAction,
			"ctrl KP_LEFT", DefaultEditorKit.previousWordAction, "ctrl RIGHT", DefaultEditorKit.nextWordAction,
			"ctrl KP_RIGHT", DefaultEditorKit.nextWordAction, "ctrl shift LEFT",
			DefaultEditorKit.selectionPreviousWordAction, "ctrl shift KP_LEFT",
			DefaultEditorKit.selectionPreviousWordAction, "ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction,
			"ctrl shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction, "ctrl A", DefaultEditorKit.selectAllAction,
			"HOME", DefaultEditorKit.beginLineAction, "END", DefaultEditorKit.endLineAction, "shift HOME",
			DefaultEditorKit.selectionBeginLineAction, "shift END", DefaultEditorKit.selectionEndLineAction,

			"UP", DefaultEditorKit.upAction, "KP_UP", DefaultEditorKit.upAction, "DOWN", DefaultEditorKit.downAction,
			"KP_DOWN", DefaultEditorKit.downAction, "PAGE_UP", DefaultEditorKit.pageUpAction, "PAGE_DOWN",
			DefaultEditorKit.pageDownAction, "shift PAGE_UP", "selection-page-up", "shift PAGE_DOWN",
			"selection-page-down", "ctrl shift PAGE_UP", "selection-page-left", "ctrl shift PAGE_DOWN",
			"selection-page-right", "shift UP", DefaultEditorKit.selectionUpAction, "shift KP_UP",
			DefaultEditorKit.selectionUpAction, "shift DOWN", DefaultEditorKit.selectionDownAction, "shift KP_DOWN",
			DefaultEditorKit.selectionDownAction, "ENTER", DefaultEditorKit.insertBreakAction, "BACK_SPACE",
			DefaultEditorKit.deletePrevCharAction, "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "ctrl H",
			DefaultEditorKit.deletePrevCharAction, "DELETE", DefaultEditorKit.deleteNextCharAction, "ctrl DELETE",
			DefaultEditorKit.deleteNextWordAction, "ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction, "RIGHT",
			DefaultEditorKit.forwardAction, "LEFT", DefaultEditorKit.backwardAction, "KP_RIGHT",
			DefaultEditorKit.forwardAction, "KP_LEFT", DefaultEditorKit.backwardAction, "TAB",
			DefaultEditorKit.insertTabAction, "ctrl BACK_SLASH", "unselect"/* DefaultEditorKit.unselectAction */,
			"ctrl HOME", DefaultEditorKit.beginAction, "ctrl END", DefaultEditorKit.endAction, "ctrl shift HOME",
			DefaultEditorKit.selectionBeginAction, "ctrl shift END", DefaultEditorKit.selectionEndAction, "ctrl T",
			"next-link-action", "ctrl shift T", "previous-link-action", "ctrl SPACE", "activate-link-action",
			"control shift O", "toggle-componentOrientation"/* DefaultEditorKit.toggleComponentOrientation */
	});

	public MaterialLookAndFeel(boolean dark) {
		colors = new MaterialColors(dark);
	}

	@Override
	protected void initComponentDefaults(UIDefaults table) {
		super.initComponentDefaults(table);

		table.put("Button.highlight", MaterialColors.GRAY_200);
		table.put("Button.opaque", true);
		table.put("Button.border", BorderFactory.createEmptyBorder(7, 17, 7, 17));
		table.put("Button.background", colors.currentPrimary);
		table.put("Button.foreground", colors.currentText);
		table.put("Button.font", MaterialFonts.REGULAR);
		table.put("Button.mouseHoverColor", colors.currentPrimaryDark);
		table.put("Button.mouseHoverEnable", true);

		table.put("CheckBox.font", MaterialFonts.REGULAR);
		table.put("CheckBox.background", colors.currentBackground);
		table.put("CheckBox.foreground", colors.currentText);
		table.put("CheckBox.icon", new ImageIcon(MaterialImages.UNCHECKED_BOX));
		table.put("CheckBox.selectedIcon", new ImageIcon(MaterialImages.PAINTED_CHECKED_BOX));

		table.put("ComboBox.font", MaterialFonts.REGULAR);
		table.put("ComboBox.background", colors.currentAccent);
		table.put("ComboBox.foreground", colors.currentText);
		table.put("ComboBox.border", BorderFactory.createCompoundBorder(MaterialBorders.LIGHT_LINE_BORDER,
				BorderFactory.createEmptyBorder(0, 5, 0, 0)));
		table.put("ComboBox.buttonBackground", MaterialColors.GRAY_300);
		table.put("ComboBox.selectionBackground", colors.currentBackground);
		table.put("ComboBox.selectionForeground", colors.currentText);
		table.put("ComboBox.selectedInDropDownBackground", MaterialColors.GRAY_200);
		table.put("ComboBox.mouseHoverColor", MaterialColors.GRAY_400);
		table.put("ComboBox.mouseHoverEnabled", true);

		table.put("Label.font", MaterialFonts.REGULAR);
		table.put("Label.background", colors.currentBackground);
		table.put("Label.foreground", colors.currentText);
		table.put("Label.border", BorderFactory.createEmptyBorder());

		table.put("Table.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy", "ctrl V",
				"paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy",
				"shift INSERT", "paste", "shift DELETE", "cut", "RIGHT", "selectNextColumn", "KP_RIGHT",
				"selectNextColumn", "shift RIGHT", "selectNextColumnExtendSelection", "shift KP_RIGHT",
				"selectNextColumnExtendSelection", "ctrl shift RIGHT", "selectNextColumnExtendSelection",
				"ctrl shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl RIGHT", "selectNextColumnChangeLead",
				"ctrl KP_RIGHT", "selectNextColumnChangeLead", "LEFT", "selectPreviousColumn", "KP_LEFT",
				"selectPreviousColumn", "shift LEFT", "selectPreviousColumnExtendSelection", "shift KP_LEFT",
				"selectPreviousColumnExtendSelection", "ctrl shift LEFT", "selectPreviousColumnExtendSelection",
				"ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl LEFT",
				"selectPreviousColumnChangeLead", "ctrl KP_LEFT", "selectPreviousColumnChangeLead", "DOWN",
				"selectNextRow", "KP_DOWN", "selectNextRow", "shift DOWN", "selectNextRowExtendSelection",
				"shift KP_DOWN", "selectNextRowExtendSelection", "ctrl shift DOWN", "selectNextRowExtendSelection",
				"ctrl shift KP_DOWN", "selectNextRowExtendSelection", "ctrl DOWN", "selectNextRowChangeLead",
				"ctrl KP_DOWN", "selectNextRowChangeLead", "UP", "selectPreviousRow", "KP_UP", "selectPreviousRow",
				"shift UP", "selectPreviousRowExtendSelection", "shift KP_UP", "selectPreviousRowExtendSelection",
				"ctrl shift UP", "selectPreviousRowExtendSelection", "ctrl shift KP_UP",
				"selectPreviousRowExtendSelection", "ctrl UP", "selectPreviousRowChangeLead", "ctrl KP_UP",
				"selectPreviousRowChangeLead", "HOME", "selectFirstColumn", "shift HOME",
				"selectFirstColumnExtendSelection", "ctrl shift HOME", "selectFirstRowExtendSelection", "ctrl HOME",
				"selectFirstRow", "END", "selectLastColumn", "shift END", "selectLastColumnExtendSelection",
				"ctrl shift END", "selectLastRowExtendSelection", "ctrl END", "selectLastRow", "PAGE_UP",
				"scrollUpChangeSelection", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP",
				"scrollLeftExtendSelection", "ctrl PAGE_UP", "scrollLeftChangeSelection", "PAGE_DOWN",
				"scrollDownChangeSelection", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN",
				"scrollRightExtendSelection", "ctrl PAGE_DOWN", "scrollRightChangeSelection", "TAB",
				"selectNextColumnCell", "shift TAB", "selectPreviousColumnCell", "ENTER", "selectNextRowCell",
				"shift ENTER", "selectPreviousRowCell", "ctrl A", "selectAll", "ctrl SLASH", "selectAll",
				"ctrl BACK_SLASH", "clearSelection", "ESCAPE", "cancel", "F2", "startEditing", "SPACE",
				"addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE",
				"moveSelectionTo", "F8", "focusHeader" }));

		table.put("Menu.font", MaterialFonts.REGULAR);
		table.put("Menu.border", BorderFactory.createEmptyBorder(5, 5, 5, 5));
		table.put("Menu.background", colors.currentAccent);
		table.put("Menu.foreground", colors.currentText);
		table.put("Menu.opaque", true);
		table.put("Menu.selectionBackground", colors.currentPrimaryLight);
		table.put("Menu.selectionForeground", colors.currentText);
		table.put("Menu.disabledForeground", new Color(0, 0, 0, 100));
		table.put("Menu.menuPopupOffsetY", 3);

		table.put("MenuBar.font", MaterialFonts.REGULAR);
		table.put("MenuBar.background", colors.currentAccent);
		table.put("MenuBar.border", MaterialBorders.LIGHT_SHADOW_BORDER);
		table.put("MenuBar.foreground", colors.currentText);

		table.put("MenuItem.disabledForeground", new Color(0, 0, 0, 100));
		table.put("MenuItem.selectionBackground", colors.currentPrimaryLight);
		table.put("MenuItem.selectionForeground", colors.currentText);
		table.put("MenuItem.font", MaterialFonts.REGULAR);
		table.put("MenuItem.background", colors.currentAccent);
		table.put("MenuItem.foreground", colors.currentText);
		table.put("MenuItem.border", BorderFactory.createEmptyBorder(5, 0, 5, 0));

		table.put("OptionPane.foreground", colors.currentText);
		table.put("OptionPane.background", colors.currentBackground);
		table.put("OptionPane.border", MaterialBorders.DEFAULT_SHADOW_BORDER);
		table.put("OptionPane.font", MaterialFonts.REGULAR);

		table.put("Panel.font", MaterialFonts.REGULAR);
		table.put("Panel.background", colors.currentBackground);
		table.put("Panel.border", BorderFactory.createEmptyBorder());

		table.put("PopupMenu.border", MaterialBorders.LIGHT_LINE_BORDER);
		table.put("PopupMenu.background", colors.currentBackground);
		table.put("PopupMenu.foreground", colors.currentText);

		table.put("RadioButton.font", MaterialFonts.REGULAR);
		table.put("RadioButton.background", colors.currentBackground);
		table.put("RadioButton.foreground", colors.currentText);
		table.put("RadioButton.icon", new ImageIcon(MaterialImages.RADIO_BUTTON_OFF));
		table.put("RadioButton.selectedIcon", new ImageIcon(MaterialImages.RADIO_BUTTON_ON));

		table.put("Spinner.font", MaterialFonts.REGULAR);
		table.put("Spinner.background", colors.currentBackground);
		table.put("Spinner.foreground", colors.currentText);
		table.put("Spinner.border", MaterialBorders.LIGHT_LINE_BORDER);
		table.put("Spinner.arrowButtonBackground", MaterialColors.GRAY_200);
		table.put("Spinner.arrowButtonBorder", BorderFactory.createEmptyBorder());
		table.put("Spinner.mouseHoverEnabled", true);
		table.put("Spinner.mouseHoverColor", MaterialColors.GRAY_400);

		table.put("ScrollBar.font", MaterialFonts.REGULAR);
		table.put("ScrollBar.track", colors.currentAccent);
		table.put("ScrollBar.thumb", colors.currentPrimary);
		table.put("ScrollBar.thumbDarkShadow", colors.currentPrimary);
		table.put("ScrollBar.thumbHighlight", colors.currentPrimary);
		table.put("ScrollBar.thumbShadow", colors.currentPrimary);
		table.put("ScrollBar.arrowButtonBackground", colors.currentPrimary);
		table.put("ScrollBar.arrowButtonBorder", BorderFactory.createEmptyBorder());

		table.put("ScrollPane.background", colors.currentBackground);
		table.put("ScrollPane.foreground", colors.currentText);
		table.put("ScrollPane.border", BorderFactory.createEmptyBorder());
		table.put("ScrollPane.font", MaterialFonts.REGULAR);
		table.put("ScrollPane.viewportBorder", BorderFactory.createEmptyBorder());

		table.put("Viewport.background", colors.currentBackground);
		table.put("Viewport.foreground", colors.currentText);

		table.put("Slider.font", MaterialFonts.REGULAR);
		table.put("Slider.background", colors.currentBackground);
		table.put("Slider.foreground", MaterialColors.LIGHT_BLUE_400);
		table.put("Slider.trackColor", colors.currentText);
		table.put("Slider.border", BorderFactory.createCompoundBorder(MaterialBorders.LIGHT_LINE_BORDER,
				BorderFactory.createEmptyBorder(20, 20, 20, 20)));

		table.put("SplitPane.border", BorderFactory.createEmptyBorder());
		table.put("SplitPane.background", colors.currentBackground);
		table.put("SplitPane.dividerSize", 5);
		table.put("SplitPaneDivider.border", BorderFactory.createEmptyBorder());

		table.put("TabbedPane.font", MaterialFonts.REGULAR);
		table.put("TabbedPane.background", colors.currentBackground);
		table.put("TabbedPane.foreground", colors.currentText);
		table.put("TabbedPane.border", BorderFactory.createEmptyBorder());
		table.put("TabbedPane.shadow", null);
		table.put("TabbedPane.darkShadow", null);
		table.put("TabbedPane.highlight", MaterialColors.GRAY_200);
		table.put("TabbedPane.borderHighlightColor", MaterialColors.GRAY_300);

		table.put("Table.selectionBackground", colors.currentBackground);
		table.put("Table.selectionForeground", colors.currentText);
		table.put("Table.background", colors.currentAccent);
		table.put("Table.foreground", colors.currentText);
		table.put("Table.font", MaterialFonts.REGULAR);
		table.put("Table.border", MaterialBorders.LIGHT_LINE_BORDER);
		table.put("Table.gridColor", MaterialColors.GRAY_200);
		table.put("TableHeader.background", colors.currentPrimary);
		table.put("TableHeader.foreground", colors.currentText);
		table.put("TableHeader.font", MaterialFonts.REGULAR);
		table.put("TableHeader.cellBorder", BorderFactory.createCompoundBorder(MaterialBorders.LIGHT_LINE_BORDER,
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		table.put("TextArea.background", MaterialColors.GRAY_200);
		table.put("TextArea.border", BorderFactory.createEmptyBorder());
		table.put("TextArea.foreground", colors.currentText);

		table.put("TextField.inactiveForeground", colors.currentText);
		table.put("TextField.inactiveBackground", colors.currentPrimary);
		table.put("TextField.selectionBackground", colors.currentPrimary);
		table.put("TextField.selectionForeground", colors.currentText);
		table.put("TextField.border", BorderFactory.createEmptyBorder(3, 5, 2, 5));

		table.put("TextArea.focusInputMap", multilineInputMap);
		table.put("TextPane.focusInputMap", multilineInputMap);

		table.put("ToggleButton.border", BorderFactory.createEmptyBorder());
		table.put("ToggleButton.font", MaterialFonts.REGULAR);
		table.put("ToggleButton.background", colors.currentBackground);
		table.put("ToggleButton.foreground", colors.currentText);
		table.put("ToggleButton.icon", new ImageIcon(MaterialImages.TOGGLE_BUTTON_OFF));
		table.put("ToggleButton.selectedIcon", new ImageIcon(MaterialImages.TOGGLE_BUTTON_ON));

		table.put("ToolBar.font", MaterialFonts.REGULAR);
		table.put("ToolBar.background", colors.currentBackground);
		table.put("ToolBar.foreground", colors.currentText);
		table.put("ToolBar.border", MaterialBorders.LIGHT_SHADOW_BORDER);
		table.put("ToolBar.dockingBackground", MaterialColors.LIGHT_GREEN_A100);
		table.put("ToolBar.floatingBackground", MaterialColors.GRAY_200);

		table.put("Tree.font", MaterialFonts.REGULAR);
		table.put("Tree.selectionForeground", colors.currentText);
		table.put("Tree.foreground", colors.currentText);
		table.put("Tree.selectionBackground", MaterialColors.GRAY_200);
		table.put("Tree.background", colors.currentBackground);
		table.put("Tree.closedIcon", new ImageIcon(MaterialImages.RIGHT_ARROW));
		table.put("Tree.openIcon", new ImageIcon(MaterialImages.DOWN_ARROW));
		table.put("Tree.selectionBorderColor", null);

		// table.put("InternalFrame.border", MaterialBorders.frameBorder);
		// table.put( "InternalFrame.titleFont", MaterialFonts.REGULAR);

		table.put("RadioButtonMenuItem.foreground", colors.currentText);
		table.put("RadioButtonMenuItem.selectionForeground", colors.currentText);
		// If it changes the background of the menuitem it must change this too,
		// irrespective of its setting
		table.put("RadioButtonMenuItem.background", UIManager.getColor("MenuItem.background"));
		table.put("RadioButtonMenuItem.selectionBackground", colors.currentPrimaryLight);
		table.put("RadioButtonMenuItem.border", BorderFactory.createEmptyBorder(5, 5, 5, 5));
		table.put("RadioButtonMenuItem.checkIcon", new ImageIcon(MaterialImages.RADIO_BUTTON_OFF));
		table.put("RadioButtonMenuItem.selectedCheckIcon", new ImageIcon(MaterialImages.RADIO_BUTTON_ON));
		table.put("RadioButtonMenuItem.selectionForeground", colors.currentText);

		// If it changes the background of the menuitem it must change this too,
		// irrespective of its setting
		table.put("CheckBoxMenuItem.background", UIManager.getColor("MenuItem.background"));
		table.put("CheckBoxMenuItem.selectionBackground", colors.currentPrimaryLight);
		table.put("CheckBoxMenuItem.selectionForeground", colors.currentText);
		table.put("CheckBoxMenuItem.foreground", colors.currentText);
		table.put("CheckBoxMenuItem.border", BorderFactory.createEmptyBorder(5, 5, 5, 5));
		table.put("CheckBoxMenuItem.checkIcon", new ImageIcon(MaterialImages.UNCHECKED_BOX));
		table.put("CheckBoxMenuItem.selectedCheckIcon", new ImageIcon(MaterialImages.OUTLINED_CHECKED_BOX));

		table.put("TextPane.border", MaterialBorders.LIGHT_LINE_BORDER);
		table.put("TextPane.background", colors.currentAccent);
		table.put("TextPane.selectionBackground", MaterialColors.LIGHT_BLUE_200);
		table.put("TextPane.inactiveForeground", MaterialColors.GRAY_500);
		table.put("TextPane.font", MaterialFonts.REGULAR);

		table.put("EditorPane.border", MaterialBorders.LIGHT_LINE_BORDER);
		table.put("EditorPane.background", MaterialColors.GRAY_50);
		table.put("EditorPane.selectionBackground", MaterialColors.LIGHT_BLUE_200);
		table.put("EditorPane.inactiveForeground", MaterialColors.GRAY_500);
		table.put("EditorPane.font", MaterialFonts.REGULAR);

		table.put("Separator.background", MaterialColors.GRAY_300);
		table.put("Separator.foreground", MaterialColors.GRAY_300);

		table.put("ToolTip.background", MaterialColors.GRAY_500);
		table.put("ToolTip.foreground", MaterialColors.GRAY_50);
		table.put("ToolTip.border", BorderFactory.createEmptyBorder(5, 5, 5, 5));

		table.put("ColorChooser.background ", colors.currentAccent);
		table.put("ColorChooser.foreground ", MaterialColors.BLACK);

		table.put("TitledBorder.font", MaterialFonts.REGULAR);

		table.put("TaskPane.font", MaterialFonts.REGULAR);
		table.put("TaskPane.titleBackgroundGradientStart", MaterialColors.GRAY_300);
		table.put("TaskPane.titleBackgroundGradientEnd", MaterialColors.GRAY_500);
		table.put("TaskPane.titleOver", MaterialColors.LIGHT_BLUE_500);
		table.put("TaskPane.specialTitleOver", MaterialColors.LIGHT_BLUE_500);
		table.put("TaskPane.background", MaterialColors.LIGHT_BLUE_500);
		table.put("TaskPane.foreground", MaterialColors.BLACK);
		table.put("TaskPane.borderColor", MaterialColors.LIGHT_BLUE_500);
		table.put("TaskPane.border", MaterialBorders.DEFAULT_SHADOW_BORDER);
		table.put("TaskPane.contentBackground", MaterialColors.GRAY_50);
		table.put("TaskPane.yesCollassed", new ImageIcon(MaterialImages.YES_COLLASSED));
		table.put("TaskPane.noCollassed", new ImageIcon(MaterialImages.NO_COLLASSED));

		table.put("OptionPaneUI.warningIcon", new ImageIcon(MaterialImages.WARNING));
		table.put("OptionPaneUI.errorIcon", new ImageIcon(MaterialImages.ERROR));
		table.put("OptionPaneUI.questionIcon", new ImageIcon(MaterialImages.QUESTION));
		table.put("OptionPaneUI.informationIcon", new ImageIcon(MaterialImages.INFORMATION));

		table.put("FormattedTextField.inactiveForeground", MaterialColors.GRAY_800);
		table.put("FormattedTextField.inactiveBackground", MaterialColors.GRAY_200);
		table.put("FormattedTextField.selectionBackground", MaterialColors.LIGHT_BLUE_400);
		table.put("FormattedTextField.selectionForeground", colors.currentText);
		table.put("FormattedTextField.border", BorderFactory.createEmptyBorder(3, 5, 2, 5));
	}

}
