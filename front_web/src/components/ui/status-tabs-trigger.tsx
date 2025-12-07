import * as TabsPrimitive from "@radix-ui/react-tabs";
import * as React from "react";
import { cn } from "@/lib/utils";
import { OrderStatus } from "@/types/order";

interface StatusTabsTriggerProps extends React.ComponentPropsWithoutRef<typeof TabsPrimitive.Trigger> {
  status?: OrderStatus | 'all';
}

const statusColorMap = {
  all: {
    active: "data-[state=active]:bg-gray-100 data-[state=active]:text-foreground/90 data-[state=active]:border-foreground/20",
    inactive: "border border-input bg-background hover:bg-gray-100 hover:text-foreground"
  },
  received: {
    active: "data-[state=active]:bg-status-received-secondary data-[state=active]:text-status-received-primary data-[state=active]:border-status-received-primary",
    inactive: "border-input transition-all duration-[.5s] ease-in-out hover:border-status-received-primary hover:text-status-received-primary hover:bg-status-received-secondary"
  },
  preparing: {
    active: "data-[state=active]:bg-status-preparing-secondary data-[state=active]:text-status-preparing-primary data-[state=active]:border-status-preparing-primary",
    inactive: "border-input transition-all duration-[.5s] ease-in-out hover:border-status-preparing-primary hover:text-status-preparing-primary hover:bg-status-preparing-secondary"
  },
  ready: {
    active: "data-[state=active]:bg-status-ready-secondary data-[state=active]:text-status-ready-primary data-[state=active]:border-status-ready-primary",
    inactive: "border-input transition-all duration-[.5s] ease-in-out hover:border-status-ready-primary hover:text-status-ready-primary hover:bg-status-ready-secondary"
  },
  delivered: {
    active: "data-[state=active]:bg-status-delivered-secondary data-[state=active]:text-status-delivered-primary data-[state=active]:border-status-delivered-primary",
    inactive: "border-input transition-all duration-[.5s] ease-in-out hover:border-status-delivered-primary hover:text-status-delivered-primary hover:bg-status-delivered-secondary"
  },
  canceled: {
    active: "data-[state=active]:bg-status-canceled-secondary data-[state=active]:text-status-canceled-primary data-[state=active]:border-status-canceled-primary",
    inactive: "border-input transition-all duration-[.5s] ease-in-out hover:border-status-canceled-primary hover:text-status-canceled-primary hover:bg-status-canceled-secondary"
  },
};

export const StatusTabsTrigger = React.forwardRef<
  React.ElementRef<typeof TabsPrimitive.Trigger>,
  StatusTabsTriggerProps
>(({ className, status = 'all', ...props }, ref) => {
  const colors = statusColorMap[status] || statusColorMap.all;

  return (
    <TabsPrimitive.Trigger
      ref={ref}
      className={cn(
        "inline-flex items-center justify-center whitespace-nowrap rounded-md px-3 py-2 text-sm font-medium border-solid border ring-offset-background transition-all data-[state=active]:shadow-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50",
        colors.active,
        colors.inactive,
        className,
      )}
      {...props}
    />
  );
});

StatusTabsTrigger.displayName = "StatusTabsTrigger";
