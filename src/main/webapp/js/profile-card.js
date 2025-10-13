function addCard() {
    let container = document.getElementById("cards_container")

    let card_div = document.createElement("div")
    card_div.className = "card_item"

    let question = document.getElementById("question_textarea").value
    let answer = document.getElementById("answer_textarea").value

    card_div.innerHTML = `
        <label>Вопрос:</label>
        <textarea type="text" name="questions[]" placeholder="Введите текст вопроса..." required>${question}</textarea>
        <label>Ответ:</label>
        <textarea type="text" name="answers[]" placeholder="Введите текст ответа..." required>${answer}</textarea>
        <button type="button" onclick="removeCard(this)">-</button>
    `

    container.appendChild(card_div)
}

function removeCard(button) {
    button.parentElement.remove()
}
