package javaswingdev.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javaswingdev.GoogleMaterialDesignIcon;
import javaswingdev.swing.scroll.ScrollBar;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

public class Menu extends JPanel {

    private int index = -1;
    private final List<EventMenuSelected> events = new ArrayList<>();

    public Menu() {
        init();
    }

    private void init() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        JScrollPane scroll = createScroll();
        panelMenu = createPanelMenu();
        scroll.setViewportView(panelMenu);
        scroll.getViewport().setOpaque(false);
        scroll.setViewportBorder(null);
        add(scroll);
        addTitle("MAIN");
        addMenuItem(new ModelMenuItem(GoogleMaterialDesignIcon.DASHBOARD, "Trang Chủ"));
        addTitle("DỊCH VỤ ");
        addMenuItem(new ModelMenuItem(GoogleMaterialDesignIcon.MONETIZATION_ON, "Nhận Xe"));
        addMenuItem(new ModelMenuItem(GoogleMaterialDesignIcon.DIRECTIONS_CAR, "Trả Xe"));
        addMenuItem(new ModelMenuItem(GoogleMaterialDesignIcon.LOYALTY, "Đăng Ký Vé Tháng"));
        addTitle("QUẢN LÝ");
        addMenuItem(new ModelMenuItem(GoogleMaterialDesignIcon.ACCOUNT_CIRCLE, "Nhân Viên "));
        addMenuItem(new ModelMenuItem(GoogleMaterialDesignIcon.GROUP, " Khách Hàng"));
        addTitle("DOANH THU");
        addMenuItem(new ModelMenuItem(GoogleMaterialDesignIcon.TODAY, "  Thống Kê Ngày"));
         addMenuItem(new ModelMenuItem(GoogleMaterialDesignIcon.TODAY, "  Thống Kê Tháng"));
        addTitle("LIÊN HỆ");
                addMenuItem(new ModelMenuItem(GoogleMaterialDesignIcon.SETTINGS, " Cài Đặt"));
        addMenuItem(new ModelMenuItem(GoogleMaterialDesignIcon.LOCK_OUTLINE, "Tài Khoản", "Đăng Xuất", "Đổi Mật Khẩu"));
        addMenuItem(new ModelMenuItem(GoogleMaterialDesignIcon.ERROR_OUTLINE, "Error", "Lỗi Hiển Thị", "Lỗi Đăng Nhập"));
    }

    private JScrollPane createScroll() {
        JScrollPane scroll = new JScrollPane();
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBar(new ScrollBar());
        return scroll;
    }

    private JPanel createPanelMenu() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        menuLayout = new MigLayout("wrap,fillx,inset 0,gapy 0", "[fill]");
        panel.setLayout(menuLayout);

        return panel;
    }

    private JPanel createMenuItem(ModelMenuItem item) {
        MenuItem menuItem = new MenuItem(item, ++index, menuLayout);
        menuItem.addEvent(new EventMenuSelected() {
            @Override
            public void menuSelected(int index, int indexSubMenu) {
                if (!menuItem.isHasSubMenu() || indexSubMenu != 0) {
                    clearSelected();
                    setSelectedIndex(index, indexSubMenu);
                }
            }
        });
//         JLabel label = menuItem.getLabel();
//
//    // Thiết lập font và kích thước cho nhãn của menu item
//    Font menuFont = new Font("Arial", Font.BOLD, 16); // Thay đổi font và kích thước theo ý muốn
//    label.setFont(menuFont);

    return menuItem;
        
        
        
        
        

    }

    private void runEvent(int index, int indexSubMenu) {
        for (EventMenuSelected event : events) {
            event.menuSelected(index, indexSubMenu);
        }
    }

    //  Public Method
    public void addMenuItem(ModelMenuItem menu) {
  

        panelMenu.add(createMenuItem(menu), "h 35!");
        
    }

    public void addTitle(String title) {
        JLabel label = new JLabel(title);
        label.setBorder(new EmptyBorder(15, 20, 5, 5));
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        label.setForeground(new Color(170, 170, 170));
        panelMenu.add(label);
    }

    public void addSpace(int size) {
        panelMenu.add(new JLabel(), "h " + size + "!");
    }

    public void setSelectedIndex(int index, int indexSubMenu) {
        for (Component com : panelMenu.getComponents()) {
            if (com instanceof MenuItem) {
                MenuItem item = (MenuItem) com;
                if (item.getIndex() == index) {
                    item.setSelectedIndex(indexSubMenu);
                    runEvent(index, indexSubMenu);
                    break;
                }
            }
        }
    }

    public void clearSelected() {
        for (Component com : panelMenu.getComponents()) {
            if (com instanceof MenuItem) {
                MenuItem item = (MenuItem) com;
                item.clearSelected();
            }
        }
    }

    public void addEvent(EventMenuSelected event) {
        events.add(event);
    }

    private MigLayout menuLayout;
    private JPanel panelMenu;
}
