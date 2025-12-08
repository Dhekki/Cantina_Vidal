import { LayoutDashboard, Package, ShoppingBag, ChevronLeft } from "lucide-react";
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
import { Button } from "@/components/ui/button";

const menuItems = [
  { title: "Dashboard",      url: "/staff/dashboard",  icon: LayoutDashboard },
  { title: "Produtos",       url: "/staff/products",   icon: Package },
  { title: "Pedido Interno", url: "/staff/orders/new", icon: ShoppingBag },
];

export function AppSidebar() {
  const { open, toggleSidebar } = useSidebar();

  return (
    <Sidebar collapsible="icon">
      <SidebarContent>
        <SidebarGroup>
          <div className="flex items-center justify-between">
            <SidebarGroupLabel>Opções do Admin</SidebarGroupLabel>
            {open && (
              <Button
                variant="ghost"
                size="icon"
                onClick={toggleSidebar}
                className="h-8 w-8 mr-2"
                title="Minimizar sidebar"
              >
                <ChevronLeft className="h-4 w-4" />
              </Button>
            )}
          </div>
          <SidebarGroupContent>
            <SidebarMenu>
              {menuItems.map((item) => (
                <SidebarMenuItem key={item.title}>
                  <SidebarMenuButton asChild tooltip={item.title}>
                    <NavLink 
                      to={item.url} 
                      end 
                      className="hover:bg-accent py-6 px-4 text-lg font-medium text-menu-color gap-4 items-center border border-transparent"
                      activeClassName="bg-accent text-accent-foreground border border-accent-foreground"
                    >
                      <span className="icon w-5">
                        <item.icon />
                      </span>
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