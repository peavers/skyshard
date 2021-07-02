# Skyshard
A computer vision based automator 

## What
Skyshard is a small Java service to continuously monitor your screen looking for a specific target, when a target is 
found on your screen it will preform an action. For example in World of Warcraft we can enable enemy name plates. 
This gives us a nice red bar on the screen to look for. If we find that red bar we know we've found an enemy we can 
then attack by sending a keystroke command. 

## How it works
Starting with a template image (src/main/resources/templates), take a screenshot of the screen every 250ms and see if 
we can find the template image anywhere in the screenshot. If we find it, move the mouse to the location of the 
template, and in this case emulate the keyboard button `1` being pressed, 

## Why use this over a mouse macro that just moves to point a then b then c
It's trivial to record your mouse movements and just replay them back, but what if something in the environment 
changes? It also makes your mouse movements very predictable. Using OpenCV we can be smarter about it and only move 
the mouse to something we know is actually there. 

## Configuration
```yaml
skyshard:
  # If true, each screenshot and the region matched will be written to the .debug directory. Slows the process down
  # a application down drastically. Not recommended unless you want to see whats happening.
  debug: false

  # When a screenshot is taken we have to decide do we want to process all the targets found in that screenshot or just
  # the first one we find, then take another screenshot. There are pros and cons to each approach. Typically, I've found
  # using `singleTargetMode` to preform better and yield better results but your mileage may very here.
  single-target-mode: true

  # Location the JVM can access a file to use for matching on the screenshot. Should be as small as possible and ideally
  # a solid colour to match on.
  template: "templates/example.png"

  # How strict between matching, between 0 and 1. 0.9 or 0.8 seem to work best.
  match-threshold: 0.9

  # The number of pixels around a successful match to ignore. This is useful if you find the entire healthbar is
  # being matched multiple times as each segment matches the template.
  duplicate-threshold: 30
```

## Warning
This was built for educational/learning purposes, this will likely get you in trouble if you attempt to use it for 
online games.
