import { useState } from "react";
import { Input } from "@/components/ui/input";
import SearchIcon from "../../../public/imgs/input-icons/search-icon.svg";

function SearchInput() {
    const [searchQuery, setSearchQuery] = useState("");

    return (
        <div className="relative w-full">
            <div className="w-[44px] h-full absolute top-1/2 border border-primary bg-primary text-primary-foreground hover:bg-primary/90 -translate-y-1/2 flex justify-center items-center rounded-lg">
                <img
                    src={SearchIcon}
                    alt=""
                    aria-hidden="true"
                />
            </div>

            <Input
                placeholder="Buscar por nome ou código."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-14"
            />
        </div>
    );
}

export default SearchInput;