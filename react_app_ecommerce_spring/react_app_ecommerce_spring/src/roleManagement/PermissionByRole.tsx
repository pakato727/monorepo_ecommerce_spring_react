import type { ReactNode } from "react"
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import type { RootState } from "../redux/store";


type RoleProps = {
    allowByRole: [string] | [string, string] | [string, string, string];
    children: ReactNode;
    
}

export default function PermissionByRole({allowByRole, children}: RoleProps){
    const roles = useSelector((state : RootState) => state.role.roles )
    //const [messageLocation, setMessageLocation] = useState("")

    const navigate = useNavigate()

    return roles.some((role :string) =>  allowByRole.includes(role) ? children : navigate("/unauthorized", {
        state: {
            messageLocation: "Siamo spiacenti! NON disponi dell'autorizzazione necessaria per raggiungere questo percorso."
        }
    }))

    
}