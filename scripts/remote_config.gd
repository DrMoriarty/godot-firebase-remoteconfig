extends Node

signal loaded
var _rc = null

func _ready():
    if type_exists('FirebaseRemoteConfig'):
        _rc = ClassDB.instance('FirebaseRemoteConfig')
    elif Engine.has_singleton('FirebaseRemoteConfig'):
        _rc = Engine.get_singleton('FirebaseRemoteConfig')
    else:
        push_warning('FirebaseRemoteConfig module not found!')
    if _rc != null:
        _rc.connect('loaded', self, '_on_loaded')

func get_boolean(param_name, default = false):
    if _rc != null:
        return _rc.get_boolean(param_name)
    else:
        return default

func get_double(param_name, default = 0.0):
    if _rc != null:
        return _rc.get_double(param_name)
    else:
        return default

func get_int(param_name, default = 0):
    if _rc != null:
        return _rc.get_int(param_name)
    else:
        return default

func get_string(param_name, default = ''):
    if _rc != null:
        return _rc.get_string(param_name)
    else:
        return default

func loaded():
    if _rc != null:
        return _rc.loaded()
    else:
        return false

func _on_loaded():
    emit_signal('loaded')

func set_defaults(defs: Dictionary) -> void:
    if _rc != null:
        _rc.setDefaults(defs)
