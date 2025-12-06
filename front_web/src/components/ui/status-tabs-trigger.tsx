import * as TabsPrimitive from "@radix-ui/react-tabs";
import * as React from "react";
import { cn } from "@/lib/utils";
import { OrderStatus } from "@/types/order";

interface StatusTabsTriggerProps extends React.ComponentPropsWithoutRef<typeof TabsPrimitive.Trigger> {
  status?: OrderStatus | 'all';
}

const statusColorMap = {
  all: {
    active: "data-[state=active]:bg-background data-[state=active]:text-foreground data-[state=active]:border-input",
    inactive: "border-input"
  },
  received: {
    active: "data-[state=active]:bg-status-received-secondary data-[state=active]:text-status-received-primary data-[state=active]:border-status-received-primary",
    inactive: "border-input"
  },
  preparing: {
    active: "data-[state=active]:bg-status-preparing-secondary data-[state=active]:text-status-preparing-primary data-[state=active]:border-status-preparing-primary",
    inactive: "border-input"
  },
  ready: {
    active: "data-[state=active]:bg-status-ready-secondary data-[state=active]:text-status-ready-primary data-[state=active]:border-status-ready-primary",
    inactive: "border-input"
  },
  delivered: {
    active: "data-[state=active]:bg-status-delivered-secondary data-[state=active]:text-status-delivered-primary data-[state=active]:border-status-delivered-primary",
    inactive: "border-input"
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
