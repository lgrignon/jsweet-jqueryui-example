package org.jsweet.examples.jquery;

import static def.jquery.Globals.$;
import static jsweet.dom.Globals.console;
import static jsweet.dom.Globals.document;
import static jsweet.dom.Globals.setInterval;
import static jsweet.dom.Globals.setTimeout;
import static jsweet.dom.Globals.window;
import static jsweet.util.StringTypes.dialog;
import static jsweet.util.StringTypes.next;
import static jsweet.util.Globals.array;
import static jsweet.util.Globals.function;

import java.util.function.Consumer;

import jsweet.lang.Array;
import jsweet.lang.Date;
import jsweet.lang.Object;
import def.jquery.JQuery;
import def.jqueryui.jqueryui.Datepicker;
import def.jqueryui.jqueryui.DatepickerOptions;

public class JQueryExample {
	
	public static void main(String[] args) {
		$(document).ready(() -> {
			JQueryExample example = new JQueryExample();
			example.start();
			return null;
		});
	}

	private final static int STEPS_COUNT = 12;

	private JQuery block1;
	private JQuery block2;

	private double blockSize;

	private String leftForMiddle;
	private String leftForBack;

	private String topForMiddle;
	private String topForBottom;

	private Consumer<JQuery>[] blockAnimationSteps = array(new Array<Consumer<JQuery>>());

	JQuery datepicker;
	JQuery menu;
	
	public JQueryExample() {

		// uncomment to test mixins
//		$(window).timer.pause();
		
		blockSize = $(window).width() / 10;

		leftForMiddle = ($(window).width() / 2 - blockSize / 2) + "px";
		leftForBack = ($(window).width() - blockSize) + "px";

		topForMiddle = ($(window).height() / 2 - blockSize / 2) + "px";
		topForBottom = ($(window).height() - blockSize) + "px";

		block1 = $(document.getElementById("block1")) //
				.css("background-color", "yellow") //
				.css("width", this.blockSize)//
				.css("line-height", this.blockSize + "px") //
				.css("font-size", (this.blockSize / 4) + "px") //
				.css("height", this.blockSize);

		// TODO : object builder ?
		block2 = $(document.getElementById("block2")) //
				.css(new Object() {
					{
						$set("background-color", "blue");
						$set("width", blockSize);
						$set("height", blockSize);
						$set("line-height", blockSize + "px");
						$set("font-size", (blockSize / 4) + "px");
						$set("top", topForBottom);
						$set("left", leftForBack);
					}
				});

		buildBlockAnimationSequence();
		
		datepicker = $("#date").datepicker(new DatepickerOptions() {
		}); 
		
		datepicker.datepicker(dialog, new Date());
		
		menu = $("#menu").menu();
		
		setInterval(function(() -> {
			console.log("next menu");
			menu.menu(next);
		}), 1000);
	}

	private void buildBlockAnimationSequence() {
		
		// back
		registerAnimationStep(0, "0", "0", "yellow");
		registerAnimationStep(1, "0", "90%", "red");

		registerAnimationStep(2, topForMiddle, leftForBack, "gray");
		registerAnimationStep(3, topForMiddle, leftForMiddle, "rgba(255,255,255,0)");
		registerAnimationStep(4, topForMiddle, "0", "gray");

		registerAnimationStep(5, topForBottom, "0", "green");
		registerAnimationStep(6, topForBottom, leftForBack, "blue");

		// and forth
		registerAnimationStep(7, topForBottom, "0", "green");

		registerAnimationStep(8, topForMiddle, "0", "gray");
		registerAnimationStep(9, topForMiddle, leftForMiddle, "rgba(255,255,255,0)");
		registerAnimationStep(10, topForMiddle, leftForBack, "gray");
		registerAnimationStep(11, "0", "90%", "red");
	}

	private void registerAnimationStep(final int index, String top, String left, String color) {
		blockAnimationSteps[index] = (block) -> {
			block.animate(new Object() {
				{
					$set("top", top);
					$set("left", left);
					$set("background-color", color);
				}
			}, 1000, function(() -> {
				blockAnimationSteps[(index + 1) % STEPS_COUNT].accept(block);
			}));
		};
	}

	public void start() {
		blockAnimationSteps[0].accept(block1);
		blockAnimationSteps[6].accept(block2);
		
	}
}
