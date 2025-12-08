import type { Config } from "tailwindcss";

export default {
  darkMode: ["class"],
  content: ["./pages/**/*.{ts,tsx}", "./components/**/*.{ts,tsx}", "./app/**/*.{ts,tsx}", "./src/**/*.{ts,tsx}"],
  prefix: "",
  theme: {
    container: {
      center: true,
      padding: "2rem",
      screens: {
        "2xl": "1400px",
      },
    },
    extend: {
      colors: {
        border: "hsl(var(--border))",
        input: "hsl(var(--input))",
        ring: "hsl(var(--ring))",
        background: "hsl(var(--background))",
        foreground: "hsl(var(--foreground))",
        menu: {
          color:  "rgb(var(--menu-item))"
        },
        primary: {
          DEFAULT: "hsl(var(--primary))",
          foreground: "hsl(var(--primary-foreground))",
        },
        secondary: {
          DEFAULT: "hsl(var(--secondary))",
          foreground: "hsl(var(--secondary-foreground))",
        },
        destructive: {
          DEFAULT: "hsl(var(--destructive))",
          foreground: "hsl(var(--destructive-foreground))",
        },
        muted: {
          DEFAULT: "hsl(var(--muted))",
          foreground: "hsl(var(--muted-foreground))",
        },
        accent: {
          DEFAULT: "rgba(var(--accent) / 0.03)",
          foreground: "rgb(var(--accent-foreground))",
        },
        popover: {
          DEFAULT: "hsl(var(--popover))",
          foreground: "hsl(var(--popover-foreground))",
        },
        card: {
          DEFAULT: "hsl(var(--card))",
          primary: "hsl(var(--primary-card))",
          foreground: "hsl(var(--card-foreground))",
        },
        status: {
          received: {
            primary:   "rgb(var(--status-received))",
            secondary: "rgba(var(--status-received) / 0.14)",
          },
          preparing: {
            primary:   "rgb(var(--status-preparing))",
            secondary: "rgba(var(--status-preparing) / 0.14)",
          },
          ready: {
            primary:   "rgb(var(--status-ready))",
            secondary: "rgba(var(--status-ready) / 0.14)",
          },
          delivered: {
            primary:   "rgb(var(--status-delivered))",
            secondary: "rgba(var(--status-delivered) / 0.14)",
          },
          canceled: {
            primary:   "rgb(var(--status-canceled))",
            secondary: "rgba(var(--status-canceled) / 0.14)",
          },
        },
        success: "hsl(var(--success))",
        info: "hsl(var(--info))",
      },
      borderRadius: {
        lg: "var(--radius)",
        md: "calc(var(--radius) - 2px)",
        sm: "calc(var(--radius) - 4px)",
      },
      keyframes: {
        "accordion-down": {
          from: {
            height: "0",
          },
          to: {
            height: "var(--radix-accordion-content-height)",
          },
        },
        "accordion-up": {
          from: {
            height: "var(--radix-accordion-content-height)",
          },
          to: {
            height: "0",
          },
        },
      },
      animation: {
        "accordion-down": "accordion-down 0.2s ease-out",
        "accordion-up": "accordion-up 0.2s ease-out",
      },
    },
  },
  plugins: [],
} satisfies Config;