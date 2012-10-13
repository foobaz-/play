###
# choices are saved like this:
# array of decision objects
# [ {balance: 10000, chance: 4.5, increase: 25, invested: 2200}, ... ]
#
###

currentQuestion = 0

gain = (chance, increase, invested) ->
  rand = Math.random() * 100 # percent
  if rand <= chance
    inrease/100*invested
  else
    -invested

getQuestion = (id) ->
  $.ajax "/decision/#{id}",
    type: "GET"
    success: (data) ->
      $("#question").html data
    error: () ->
      alert('bla')

$("#start").click ->
  getQuestion 1
  $('#controls').show()

jQuery ->

  # getQuestion(1) ## <---- funktioniert

  # set invest button handler
  #$("#invest").click ->

  # set start button handler
    # get new question
    # make controls visible
    # initialise all control variables


