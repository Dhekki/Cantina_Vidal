import * as React from "react";
import { cva, type VariantProps } from "class-variance-authority";
import { Slot } from "@radix-ui/react-slot";
import { cn } from "../../lib/utils";

const buttonVariants = cva(
  "inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-base font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50",
  {
    variants: {
      variant: {
        default: "border border-primary bg-primary text-primary-foreground hover:bg-primary/90",
        categorySelected: "border border-primary bg-primary/5 text-primary",
        outline: "border border-input bg-background text-foreground/70 hover:bg-gray-100 hover:text-foreground/80",
        addProduct: "rounded border border-input bg-background text-primary hover:bg-status-canceled-primary/20 hover:border-status-canceled-primary/50 shadow-md",
        // ...
        preparing: "bg-status-preparing-primary text-white hover:bg-status-preparing-primary/90",
        ready: "bg-status-ready-primary text-white hover:bg-status-ready-primary/90",
        delivered: "bg-status-delivered-primary text-white hover:bg-status-delivered-primary/90",
        canceled: "bg-status-canceled-primary text-white hover:bg-status-canceled-primary/90",
        notPaid: "bg-status-canceled-primary text-white hover:bg-status-canceled-primary/90",
        dangerOutline: "bg-transparent text-status-canceled-primary border border-status-canceled-primary hover:bg-status-canceled-primary/20",
        // Pop-ups buttons
        editPopUp: "bg-transparent text-status-canceled-primary border border-status-canceled-primary hover:bg-status-canceled-primary/20",
      },
      size: {
        icon: "h-10 w-10 p-0",
        default: "h-10 px-4 py-2",
        sm: "h-10 rounded-md px-3",
        lg: "h-11 rounded-md px-8",
        addProductSize: "h-11 rounded-full px-8",
        categorySize: "w-full h-fit flex items-center justify-start rounded-md py-4 px-5 gap-2",
      },
    },
    defaultVariants: {
      variant: "default",
      size: "default",
    },
  }
);

export interface ButtonProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof buttonVariants> {
  asChild?: boolean;
  icon?: React.ReactNode;
}

const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  ({ className, variant, size, asChild = false, icon, children, ...props }, ref) => {
    const Comp: React.ElementType = asChild ? Slot : "button";
    return (
      <Comp
        ref={ref as any}
        className={cn(buttonVariants({ variant, size }), className)}
        {...(props as React.ComponentPropsWithoutRef<"button">)}
      >
        {icon && <span aria-hidden="true">{icon}</span>}
        {children}
      </Comp>
    );
  }
);

Button.displayName = "Button";

export { Button, buttonVariants };