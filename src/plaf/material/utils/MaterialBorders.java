package plaf.material.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;

import com.mic.assembly.AssemblyMachine;

public class MaterialBorders {
	public static final Border LIGHT_LINE_BORDER = BorderFactory.createLineBorder (MaterialColors.GRAY_200, 1);

	public static final Border LIGHT_SHADOW_BORDER = new DropShadowBorder (Color.BLACK, 0, 4, 0.3f, 12, true, true, true, true);
	public static final Border DEFAULT_SHADOW_BORDER = new DropShadowBorder (Color.BLACK, 5, 5, 0.3f, 12, true, true, true, true);

	public final InternalFrameBorder frameBorder = new InternalFrameBorder();

	public class InternalFrameBorder extends AbstractBorder implements UIResource {
        private static final int corner = 14;

        public void paintBorder(Component c, Graphics g, int x, int y,
                          int w, int h) {

            Color background;
            Color highlight;
            Color shadow;

            if (c instanceof JInternalFrame && ((JInternalFrame)c).isSelected()) {
                background = AssemblyMachine.ui.colors.currentPrimary;
                highlight = AssemblyMachine.ui.colors.currentPrimaryLight;
                shadow = AssemblyMachine.ui.colors.currentPrimaryDark;
            } else {
            	background = AssemblyMachine.ui.colors.currentPrimary;
                highlight = AssemblyMachine.ui.colors.currentPrimaryLight;
                shadow = AssemblyMachine.ui.colors.currentPrimaryDark;
            }

              g.setColor(background);
              // Draw outermost lines
              g.drawLine( 1, 0, w-2, 0);
              g.drawLine( 0, 1, 0, h-2);
              g.drawLine( w-1, 1, w-1, h-2);
              g.drawLine( 1, h-1, w-2, h-1);

              // Draw the bulk of the border
              for (int i = 1; i < 5; i++) {
                  g.drawRect(x+i,y+i,w-(i*2)-1, h-(i*2)-1);
              }

              if (c instanceof JInternalFrame &&
                               ((JInternalFrame)c).isResizable()) {
                  g.setColor(highlight);
                  // Draw the Long highlight lines
                  g.drawLine( corner+1, 3, w-corner, 3);
                  g.drawLine( 3, corner+1, 3, h-corner);
                  g.drawLine( w-2, corner+1, w-2, h-corner);
                  g.drawLine( corner+1, h-2, w-corner, h-2);

                  g.setColor(shadow);
                  // Draw the Long shadow lines
                  g.drawLine( corner, 2, w-corner-1, 2);
                  g.drawLine( 2, corner, 2, h-corner-1);
                  g.drawLine( w-3, corner, w-3, h-corner-1);
                  g.drawLine( corner, h-3, w-corner-1, h-3);
              }

          }

          public Insets getBorderInsets(Component c, Insets newInsets) {
              newInsets.set(5, 5, 5, 5);
              return newInsets;
          }
    }
	
	
	private MaterialBorders () {}
}
