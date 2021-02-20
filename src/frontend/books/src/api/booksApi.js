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

export async function getBooksByAuthor(author) {
    const response = await axios.get("http://localhost:9000/available?author=" + author)
    console.log(response)
    console.log(response.data)
    return response.data
}
export async function getBooksByDescription(description) {
    const response = await axios.get("http://localhost:9000/available?description=" + description)
    console.log(response)
    console.log(response.data)
    return response.data
}
export async function getBooksByTitle(title) {
    const response = await axios.get("http://localhost:9000/available?title="+title)
    console.log(response)
    console.log(response.data)
    return response.data
}
