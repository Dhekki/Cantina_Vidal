import * as React from "react";
import { cn } from "../../lib/utils";

type InputProps = React.InputHTMLAttributes<HTMLInputElement> & {
  label: string;
  imageSrc?: string;
  imageAlt?: string;
};

const Input = React.forwardRef<HTMLInputElement, InputProps>(
  ({ className, type = "text", label, imageSrc, imageAlt, id, ...props }, ref) => {
    const inputId = id ?? React.useId();

    return (
      <div className="relative w-full">
        <img
          src={imageSrc}
          alt={imageAlt}
          aria-hidden="true"
          className="absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 opacity-70 z-50"
        />

        <input
          id={inputId}
          ref={ref}
          type={type}
          placeholder=" "
          className={cn(
            "peer h-10 w-full rounded-md border border-input bg-background !pl-10 pr-3 py-2 text-base ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium file:text-foreground placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring/70 focus-visible:ring-offset-1 disabled:cursor-not-allowed disabled:opacity-50 md:text-sm",
            className
          )}
          {...props}
        />

        <label
          className={cn(
            "pointer-events-none absolute left-3 top-1/2 -translate-y-1/2",
            "bg-background px-1 pl-10 font-semibold text-sm text-muted-foreground/80 transition-all",
            "peer-focus:top-0 peer-focus:text-xs peer-focus:text-muted-foreground",
            "peer-[&:not(:placeholder-shown)]:top-0 peer-[&:not(:placeholder-shown)]:text-xs"
          )}
        >
          {label}
        </label>
      </div>
    );
  }
);

Input.displayName = "Input";
export { Input };