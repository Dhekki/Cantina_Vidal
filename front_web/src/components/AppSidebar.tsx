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
  SidebarTrigger,
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
    <Sidebar onMouseEnter={toggleSidebar} onMouseLeave={toggleSidebar} collapsible="icon">
      <SidebarContent>
        <SidebarGroup>
          {open && (
            <div className="flex items-center justify-between border-1 border-b py-4">
              <SidebarGroupLabel>Opções do Admin</SidebarGroupLabel>

              <Button
                variant="outline"
                size="icon"
                onClick={toggleSidebar}
                className="h-8 w-8 mr-2 hover:bg-accent"
                title="Minimizar sidebar"
              >
                <ChevronLeft className="h-4 w-4" />
              </Button>
            </div>
          )}
          <SidebarGroupContent>
            <SidebarMenu>
              {!open && (
                <Button
                  variant="outline"
                  size="icon"
                  onClick={toggleSidebar}
                  className="h-8 w-8 mr-2 mt-4 hover:bg-accent"
                  title="Expandir sidebar"
                >
                <SidebarTrigger />
                </Button>
              )}

              {menuItems.map((item) => (
                <SidebarMenuItem key={item.title}>
                  <SidebarMenuButton asChild>
                    <NavLink 
                      to={item.url} 
                      end 
                      aria-label={item.title}
                      className="hover:bg-accent py-6 px-4 text-lg font-medium text-menu-color gap-4 items-center border border-transparent flex justify-start group-data-[collapsible=icon]:justify-center"
                      activeClassName="bg-accent text-accent-foreground border border-accent-foreground"
                    >
                      <span className="icon w-5 h-5 flex items-center justify-center shrink-0">
                        <item.icon className="w-5 h-5" />
                      </span>
                      {open && <span>{item.title}</span>}
                    </NavLink>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ))}
              {/* Calculator */}
              <SidebarMenuItem>
                <SidebarMenuButton asChild>
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