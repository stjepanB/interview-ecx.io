import axios from "axios"


export async function getBooks() {
    const response = await axios.get("http://localhost:9000/")
 
    return response.data
}

export async function getAvailableBooks() {
    const response = await axios.get("http://localhost:9000/available")
    console.log(response)
    console.log(response.data)
    return response.data
}

