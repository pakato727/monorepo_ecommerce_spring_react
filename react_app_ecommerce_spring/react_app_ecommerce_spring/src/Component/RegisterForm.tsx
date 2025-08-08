import { useState, type FormEvent } from "react"
import Home from "../pages/Home";
import Navbar from "./Navbar";


// type formDataStructure = {
//     name: string,
//     surname: string,
//     birthDate: string,
//     fiscalCode: string,
//     gender: string,
//     zipCode: number,
//     address: string,
//     city: string,
//     province: string,
//     username: string,
//     email: string,
//     password: string
// }

export default function RegisterForm() {
    const [formData, setFormData] = useState({
        name: "",        // user.name
        surname: "",     // user.surname
        birthDate: "",
        fiscalCode: "",
        gender: "",
        zipCode: "",
        address: "",
        city: "",
        province: "",
        username: "",    // account.username
        email: "",       // account.email
        password: ""    // account.password
    });

    // const [message, setMessage] = useState("")
    const [message, setMessage] = useState("");

    const handleChange = (e: FormEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.currentTarget.name]: e.currentTarget.value });
    }

    const handleSelectChange = (e: FormEvent<HTMLSelectElement>) => {
        setFormData({ ...formData, [e.currentTarget.name]: e.currentTarget.value });
    }

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        // const payload = {
        //     name: formData.name,
        //     surname: formData.surname,
        //     birthDate: formData.birthDate,
        //     fiscalCode: formData.fiscalCode,
        //     gender: formData.gender,
        //     zipCode: formData.zipCode,
        //     address: formData.address,
        //     city: formData.city,
        //     province: formData.province,
        //     username: formData.username,
        //     email: formData.email,
        //     password: formData.password
        // }

        // form.append("name", formData.name);
        // form.append("surname", formData.surname);
        // form.append("birthDate", formData.birthDate);
        // form.append("fiscalCode", formData.fiscalCode);
        // form.append("gender", formData.gender);
        // form.append("zipCode", formData.zipCode);
        // form.append("address", formData.address);
        // form.append("city", formData.city);
        // form.append("province", formData.province);
        // form.append("username", formData.username);
        // form.append("email", formData.email);
        // form.append("password", formData.password);
        const form = new FormData();
        Object.entries(formData).forEach(([key, value]) => form.append(key, value));
        try {
            console.log("SONO DENTRO AL TRY")
            const response = await fetch("http://localhost:8095/api/signIn", {
                method: "POST",
                //headers: {"Content-Type": "application/json"},
                body: form,
                credentials: "include" // solo se usi cookie/sessione, altrimenti rimuovi
            });
            //.then(response => response.json());

            if (!response.ok && response.status !== 409) {
                const errorText = await response.text();
                console.error(errorText);
                setMessage("errore nella richiesta" + response.statusText);
                console.log("SONO DENTRO ALLA CONDIZIONE VERA DELL'IF");
                return;
            }
            else if (response.status == 409) {
                console.log("SONO DENTRO ALLA CONDIZIONE ELSE IF")
                setMessage("esistente")

            }

            const data = await response.json();
            console.log(data);
            setMessage(data.message);

        } catch (err) {
            console.error("Errore nella richiesta:", err);
            setMessage("Errore di rete");
        }

    }
    if (message == "creata") {
        return (
            <Home message={`Utente creato, confermare all'indirizzo email: ${formData.email}`}></Home>
        )
    }
    else if (message == "esistente") {
        return (
            <Home message="Utente gia esistente"></Home>
        )
    } else {

        return (
            <>
                <Navbar></Navbar>
                <div className="flex justify-center items-center min-h-screen py-10 bg-gray-100">
                    <div className="bg-white p-8 rounded-2xl shadow-lg w-full max-w-4xl">
                        <h1 className="text-2xl font-bold text-center mb-6">Registrazione</h1>
                        <form onSubmit={handleSubmit} method="post" className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div>
                                <label htmlFor="nome" className="block font-medium mb-1">Nome</label>
                                <input type="text" id="nome" name="name" onChange={handleChange}
                                    pattern="[A-Za-z]+" required
                                    className="w-full border rounded px-3 py-2" />
                            </div>
                            <div>
                                <label htmlFor="cognome" className="block font-medium mb-1">Cognome</label>
                                <input type="text" id="cognome" name="surname" onChange={handleChange}
                                    pattern="[A-Za-z]+" required
                                    className="w-full border rounded px-3 py-2" />
                            </div>
                            <div>
                                <label htmlFor="date" className="block font-medium mb-1">Data di nascita</label>
                                <input type="date" id="date" name="birthDate" onChange={handleChange}
                                    onInput={(e) => {
                                        const input = e.currentTarget;
                                        input.setCustomValidity('');
                                        const value = input.valueAsDate;
                                        if (value) {
                                            const today = new Date();
                                            const eighteenYearsAgo = new Date(
                                                today.getFullYear() - 18,
                                                today.getMonth(),
                                                today.getDate()
                                            );
                                            if (value > eighteenYearsAgo) {
                                                input.setCustomValidity('Devi essere maggiorenne per registrarti.');
                                            }
                                        }
                                    }}
                                    required className="w-full border rounded px-3 py-2" />
                            </div>
                            <div>
                                <label htmlFor="codicefiscale" className="block font-medium mb-1">Codice Fiscale</label>
                                <input type="text" id="codicefiscale" name="fiscalCode" onChange={handleChange}
                                    pattern="[A-Z0-9]{16}" required
                                    className="w-full border rounded px-3 py-2" />
                            </div>
                            <div>
                                <label htmlFor="gender" className="block font-medium mb-1">Genere</label>
                                <select id="gender" name="gender" onChange={handleSelectChange} required
                                    className="w-full border rounded px-3 py-2">
                                    <option value="">Seleziona...</option>
                                    <option value="Maschio">Maschio</option>
                                    <option value="Femmina">Femmina</option>
                                    <option value="Altro">Altro</option>
                                </select>
                            </div>
                            <div>
                                <label htmlFor="zipcode" className="block font-medium mb-1">CAP</label>
                                <input type="text" id="zipcode" name="zipCode" onChange={handleChange}
                                    pattern="\d{5}" required
                                    className="w-full border rounded px-3 py-2" />
                            </div>
                            <div>
                                <label htmlFor="address" className="block font-medium mb-1">Indirizzo</label>
                                <input type="text" id="address" name="address" onChange={handleChange}
                                    pattern="[A-Za-z0-9 ,.'\-]{3,}" required
                                    className="w-full border rounded px-3 py-2" />
                            </div>
                            <div>
                                <label htmlFor="city" className="block font-medium mb-1">Città</label>
                                <input type="text" id="city" name="city" onChange={handleChange}
                                    pattern="[A-Za-z\s]+" required
                                    className="w-full border rounded px-3 py-2" />
                            </div>
                            <div>
                                <label htmlFor="province" className="block font-medium mb-1">Provincia</label>
                                <input type="text" id="province" name="province" onChange={handleChange}
                                    pattern="[A-Za-z\s]+" required
                                    className="w-full border rounded px-3 py-2" />
                            </div>
                            <div>
                                <label htmlFor="username" className="block font-medium mb-1">Username</label>
                                <input type="text" id="username" name="username" onChange={handleChange}
                                    maxLength={30} required
                                    className="w-full border rounded px-3 py-2" />
                            </div>
                            <div>
                                <label htmlFor="email" className="block font-medium mb-1">Email</label>
                                <input type="email" id="email" name="email" onChange={handleChange}
                                    required className="w-full border rounded px-3 py-2" />
                            </div>
                            <div>
                                <label htmlFor="password" className="block font-medium mb-1">Password</label>
                                <input type="password" id="password" name="password" onChange={handleChange}
                                    required className="w-full border rounded px-3 py-2" />
                            </div>
                            <div className="col-span-1 md:col-span-2">
                                <button type="submit" className="w-full bg-black text-white py-2 rounded hover:bg-gray-800 transition">
                                    Registrati
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </>
        );

    }

}