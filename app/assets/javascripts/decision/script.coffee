###
# choices are saved like this:
# array of decision objects
# [ {balance: 10000, chance: 4.5, increase: 25, invested: 2200}, ... ]
#
###

currentQuestion = 0
balance = 0

gain = (chance, increase, invested) ->
  rand = Math.random() * 100 # percent
  if rand <= chance
    inrease/100*invested
  else
    -invested

getQuestion = (id, callback) ->
  $.get "/decision/#{id}", (data) ->
    $("#question").html data
    do callback


jQuery ->
  $("#start").click ->
    getQuestion 1, -> $('#controls').show()

  # set invest button handler
  $("#invest").click ->
    # check if field is empty
    # check if there is a number
    # check if the number is smaller-equal than the current balance
    # perform


