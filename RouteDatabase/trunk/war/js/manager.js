$(function() {
	var contentWidth = 300;

	var mapContainer = $("#map"), contentContainer = $("#content");

	// Size components
	function sizeComponents() {
		var viewportWidth = $(window).width();
		var viewportHeight = $(window).height();

		mapContainer.height(viewportHeight);
		mapContainer.width(viewportWidth - contentWidth);

		contentContainer.height(viewportHeight);
		contentContainer.width(contentWidth);
	}

	sizeComponents();

	$(window).resize(sizeComponents);

	// Load map
	google.maps.visualRefresh = true;

	var mapOptions = {
		center : new google.maps.LatLng(-34.397, 150.644),
		zoom : 8,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};

	var Map = new google.maps.Map(document.getElementById("map"), mapOptions);

	var RouteCreatorActivity = {
		root : null,
		pathController : null,
		pathName : null,
		pathDuration : null,
		pathPoints : null,
		pathLenght : null,
		pathAvgSegmentLength : null,

		start : function _start() {
			// Load view
			this.root = $(".view.createPath");

			// Load fields
			this.pathName = new Field(this.root.find("#pathName"));
			this.pathDuration = new Field(this.root.find("#pathDuration"));
			this.pathPoints = new Field(this.root.find("#pathPoints"));
			this.pathLength = new Field(this.root.find("#pathLength"));
			this.pathAvgSegmentLength = new Field(this.root
					.find("#pathAvgSegmentLength"));

			var self = this;
			// Load path creator
			this.pathController = new PathController(Map, function(update) {
				self.pathPoints.set(update.numPoints);
			});

			this.root.find("#pathCompleted").click(this.onFinish.bind(this));
		},

		onFinish : function _onFinish() {
			this.stop();
		},

		stop : function _stop() {
			this.pathController.clear();
		}
	}

	RouteCreatorActivity.start();

});

function PathController(map, callback) {
	this.map = map;
	this.callback = callback;

	// Create path line
	this.pathLine = new google.maps.Polyline({
		strokeColor : '#009dff',
		strokeOpacity : 1.0,
		strokeWeight : 3
	});

	this.pathLine.setMap(map);
	this.path = this.pathLine.getPath();

	this.mapClickListener = google.maps.event.addListener(map, 'click', this.addPoint.bind(this));
}

PathController.prototype = {
	pointControllers : [],

	addPoint : function(event) {
		var position = event.latLng;
		var marker = new google.maps.Marker({
			position : position,
			map : this.map,
			draggable : true,
			draggableCursor : 'crosshair'
		});

		var index = this.path.length;
		this.path.push(position);

		var controller = new PointController(this.path, marker, index);
		this.pointControllers.push(controller);

		google.maps.event.addListener(marker, 'drag', controller.onMove.bind(controller));

		var self = this;
		google.maps.event.addListener(marker, 'click', function(event) {
			var originalEvent = event.Xa;
			if (originalEvent.ctrlKey) {
				originalEvent.preventDefault();
				var pathLength = self.path.length;
				var deletedIndex = controller.getIndex();

				controller.del();

				if (deletedIndex < (pathLength - 1)) {

					// Realign controllers to points
					for ( var i = deletedIndex + 1; i < pathLength; i++) {
						self.pointControllers[i]
								.setIndex(i - 1);
						self.pointControllers[i - 1] = self.pointControllers[i];
					}

					self.pointControllers.pop();
				}

				self.notifyListener();
			}
		});

		this.notifyListener();
	},

	notifyListener : function _notifyListener() {
		this.callback({
			numPoints : this.path.length
		});
	},

	clear : function() {
		for ( var i = 0; i < this.pointControllers.length; i++) {
			this.pointControllers[i].del();
		}

		this.pointControllers = [];
		this.pathLine.setMap(null);
		google.maps.event.removeListener(this.mapClickListener);
	}
}

function PointController(path, marker, index) {
	this.path = path;
	this.marker = marker;
	this.index = index;
}

PointController.prototype = {
	del : function _delete() {
		this.path.removeAt(this.index);
		this.marker.setMap(null);
	},
	onMove : function _onMove(event) {
		this.path.setAt(this.index, event.latLng);
	},
	setIndex : function _setIndex(index) {
		this.index = index;
		console.log("new index: " + index);
	},
	getIndex : function _getIndex() {
		return this.index;
	}
}

// Field
function Field(element) {
	this.element = element;
	this.prop = (element.is("input:text") ? element.val : element.text)
			.bind(element);
}

Field.prototype = {
	get : function() {
		return this.prop();
	},
	set : function(val) {
		this.prop(val);
	},
	clear : function() {
		this.prop("");
	}
}
