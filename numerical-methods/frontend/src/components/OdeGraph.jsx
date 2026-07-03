import React from "react";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
} from "chart.js";
import { Line } from "react-chartjs-2";

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

const colors = ["#007bff", "#28a745", "#dc3545", "#6f42c1"];

function formatLabel(value) {
  return typeof value === "number" && Number.isFinite(value) ? value.toFixed(4) : String(value);
}

export default function OdeGraph({ originalFormula, result }) {
  if (!result || !Array.isArray(result.results) || result.results.length === 0) return null;

  const firstPoints = result.results.find(res => res.points?.length)?.points || [];
  if (firstPoints.length === 0) return null;

  const labels = firstPoints.map(p => formatLabel(p.x));

  const datasets = [
    {
      label: "Точное решение",
      data: firstPoints.map(p => p.exactY),
      borderColor: "#000000",
      backgroundColor: "#000000",
      borderDash: [6, 4],
      pointRadius: 2,
      tension: 0.2
    },
    ...result.results.map((res, i) => ({
      label: res.methodName,
      data: res.points.map(p => p.y),
      borderColor: colors[i % colors.length],
      backgroundColor: colors[i % colors.length],
      borderWidth: res.methodName === result.bestMethod ? 3 : 2,
      pointRadius: res.methodName === result.bestMethod ? 3 : 2,
      tension: 0.2
    }))
  ];

  return (
    <div style={{ marginTop: 20 }}>
      <div style={{ width: "100%", height: 500, borderRadius: 8, border: "1px solid #ddd", padding: 15, boxSizing: "border-box" }}>
        <Line
          data={{ labels, datasets }}
          options={{
            responsive: true,
            maintainAspectRatio: false,
            interaction: { mode: "index", intersect: false },
            plugins: {
              legend: { position: "top" },
              title: {
                display: Boolean(originalFormula),
                text: originalFormula
              }
            },
            scales: {
              x: { title: { display: true, text: "x" } },
              y: { title: { display: true, text: "y" } }
            }
          }}
        />
      </div>
      <p style={{ fontSize: "0.85rem", color: "#666", textAlign: "center" }}>
        Черный пунктир — точное решение. Цветные линии — численные методы.
      </p>
    </div>
  );
}
