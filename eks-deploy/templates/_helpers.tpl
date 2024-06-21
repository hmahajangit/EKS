{{- define "{{ .Values.app.app_name  }}.labels" -}}
app: nw-{{ .Values.app.app_name  }}
env: {{ .Values.app.env  }}
{{- end -}}
