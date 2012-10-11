nrOfQuestions = 2
currentQuestion = 0
answers = {}


saveQ1 = () ->
  radio = $('input:checked')
  if radio.length isnt 0
    answers.q1 = radio.val()
  else
    throw "You have to select something!"

saveQ2 = () ->
  answers.q2 = $('textarea').val()

initialize = () ->
  $("#next").before(
    '<a id="previous" class="btn btn-primary btn-large" href="#">previous</a>'
  ).html("next")
  $("#introduction").remove()
  ###################################
  # Add previous-button click event #
  ###################################
  $("#previous").click ->
    if currentQuestion <= 1 then return
    else
      currentQuestion--
      # Ajax call 
      $.ajax "/favoritecolor/question" + currentQuestion,
        type: "GET"
        success: (data, status, jqxhr) ->
          $("#question").html(data)
        error: (jqxhr, status, error) ->
          console.log("ajax error: status: #{status}, error: #{error}")
          currentQuestion++
  ###################################


jQuery ->
  ###############################
  # Add next-button click event #
  ###############################
  $("#next").click ->
    try
      switch currentQuestion
        when 0 then do initialize
        when 1 then do saveQ1
        when 2 then do saveQ2
    catch error
      console.log("error while saving question #{currentQuestion}: #{error}")
      return 

    currentQuestion++

    # All questions answered, send to server
    if currentQuestion > nrOfQuestions
      $.ajax "/favoritecolor/save",
        type: 'POST'
        data: answers
        success: (data, status, jqxhr) ->
          console.log("successfully POSTed #{answers} to server")
          console.log("got response: #{data}")

    # Request next question
    else
      $('#next').html('submit!') if currentQuestion is nrOfQuestions
      # Ajax call with request for next question
      $.ajax "/favoritecolor/question" + currentQuestion,
        type: "GET"
        dataType: 'html'
        success: (data, status, jqXHR) ->
          $("#question").html(data)
        error: (jqXHR, status, error) ->
          console.log("ajax error: status: #{status}, error: #{error}")
          currentQuestion--
  ###############################
      
