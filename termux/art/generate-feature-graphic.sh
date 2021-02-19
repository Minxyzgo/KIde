#!/bin/bash

echo "Generating feature graphics to ~/termux-icons/termux-feature-graphic.png..."
mkdir -p ~/kide-icons/
rsvg-convert feature-graphic.svg > ~/kide-icons/feature-graphic.png
