import { LayoutDashboard, Package, ShoppingBag } from "lucide-react";
import { NavLink } from "@/components/NavLink";
import {
  Sidebar,
  useSidebar,
  SidebarMenu,
  SidebarGroup,
  SidebarContent,
  SidebarMenuItem,
  SidebarGroupLabel,
  SidebarMenuButton,
  SidebarGroupContent,
} from "@/components/ui/sidebar";
import { CalculatorDialog } from "@/components/CalculatorDialog";

const menuItems = [
  { title: "Dashboard",      url: "/staff/dashboard",  icon: LayoutDashboard },
  { title: "Produtos",       url: "/staff/products",   icon: Package },
  { title: "Pedido Interno", url: "/staff/orders/new", icon: ShoppingBag },
];

export function AppSidebar() {
  const { open } = useSidebar();

  return (
    <Sidebar collapsible="icon">
      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel>Canteen Operations</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              {menuItems.map((item) => (
                <SidebarMenuItem key={item.title}>
                  <SidebarMenuButton asChild tooltip={item.title}>
                    <NavLink 
                      to={item.url} 
                      end 
                      className="hover:bg-accent"
                      activeClassName="bg-accent text-accent-foreground font-medium"
                    >
                      <item.icon className="h-4 w-4" />
                      {open && <span>{item.title}</span>}
                    </NavLink>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ))}
              {/* Calculator */}
              <SidebarMenuItem>
                <SidebarMenuButton asChild tooltip="Calculadora">
                  <div className="flex items-center cursor-pointer">
                    <CalculatorDialog />
                    {open && <span className="ml-2">Calculadora</span>}
                  </div>
                </SidebarMenuButton>
              </SidebarMenuItem>
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
    </Sidebar>
  );
}