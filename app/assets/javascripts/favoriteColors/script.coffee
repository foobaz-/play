currentQuestion = 0
answers = {}

saveQ1 = () ->
  radio = $('input:checked')
  if radio.length isnt 0
    answers.q1 = radio.val()
  else
    throw "You have to select something!"

initialize = () ->
  $("#next").before(
    '<a id="previous" class="btn btn-primary btn-large" href="#">previous</a>'
  ).html("next")
  $("#introduction").remove()

jQuery ->
   # Add next-button click event
  $("#next").click ->
    try
      switch currentQuestion
        when 0 then do initialize
        when 1 then do saveQ1
    catch error
      console.log("error while saving question #{currentQuestion}: #{error}")
      return 

    currentQuestion++
    # Ajax call 
    $.ajax "/favoritecolor/question" + currentQuestion,
      type: "GET"
      dataType: 'html'
      success: (data, status, jqXHR) ->
        $("#question").html(data)
      error: (jqXHR, status, error) ->
        console.log("ajax error: status: #{status}, error: #{error}")
        currentQuestion--
      
   # Add previous-button click event
   $("#previous").click ->
    alert("clicked previous")
    if currentquestion <= 1 then return
    else
      currentquestion--
      # Ajax call 
      $.ajax "/favoritecolor/question" + currentquestion,
        type: "get"
        success: (data, status, jqxhr) ->
          $("#question").html(data)
        error: (jqxhr, status, error) ->
          console.log("ajax error: status: #{status}, error: #{error}")
          currentquestion++


